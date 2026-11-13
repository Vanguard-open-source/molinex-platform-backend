package com.molinex.platform.production;

import com.molinex.platform.production.domain.model.events.RawMaterialReceptionRecorded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:molinex-reception;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@RecordApplicationEvents
class RawMaterialReceptionIntegrationTests {

    private static final String RECEPTIONS = "/api/v1/raw-material-receptions";
    private static final String RECEPTION = """
            {"id":0,"receivedAt":"2026-10-03T11:30:00Z","supplierName":"Agro Norte",
             "originDescription":"Lambayeque","quantityValue":18.125,"quantityUnit":"METRIC_TON"}
            """;

    @Autowired
    ApplicationEvents events;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void storesReceptionAndExposesQueriesAndEvent() throws Exception {
        var response = mvc.perform(post(RECEPTIONS).contentType(MediaType.APPLICATION_JSON).content(RECEPTION))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.supplierName").value("Agro Norte"))
                .andExpect(jsonPath("$.quantityValue").value(18.125))
                .andExpect(jsonPath("$.quantityUnit").value("METRIC_TON"))
                .andReturn().getResponse();
        var location = URI.create(response.getHeader("Location")).getPath();
        var id = Long.parseLong(location.substring(RECEPTIONS.length() + 1));

        assertTrue(id > 0);
        assertTrue(events.stream(RawMaterialReceptionRecorded.class).anyMatch(event -> event.id().equals(id)));
        mvc.perform(get(location)).andExpect(status().isOk())
                .andExpect(jsonPath("$.originDescription").value("Lambayeque"));
        mvc.perform(get(RECEPTIONS)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
        mvc.perform(get(RECEPTIONS + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsMissingFieldsInvalidUnitAndWeight() throws Exception {
        for (var body : new String[]{
                "{}", RECEPTION.replace("METRIC_TON", "POUND"),
                RECEPTION.replace("18.125", "0"), RECEPTION.replace("18.125", "1.00001")
        }) {
            mvc.perform(post(RECEPTIONS).contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        assertTrue(events.stream(RawMaterialReceptionRecorded.class).findAny().isEmpty());
    }
}
