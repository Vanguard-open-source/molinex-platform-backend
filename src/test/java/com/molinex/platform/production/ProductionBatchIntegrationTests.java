package com.molinex.platform.production;

import com.molinex.platform.production.domain.model.events.ProductionBatchRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=${MOLINEX_BATCH_TEST_URL:jdbc:h2:mem:molinex-batch;MODE=MySQL;DB_CLOSE_DELAY=-1}",
        "spring.datasource.username=${MOLINEX_TEST_DB_USER:sa}",
        "spring.datasource.password=${MOLINEX_TEST_DB_PASSWORD:}",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
@RecordApplicationEvents
class ProductionBatchIntegrationTests {

    private static final String BATCHES = "/api/v1/production-batches";
    private static final String RECEPTIONS = "/api/v1/raw-material-receptions";
    private static final String RECEPTION = """
            {"receivedAt":"2026-10-03T11:30:00Z","supplierName":"Agro Norte",
             "originDescription":"Lambayeque","quantityValue":18000,"quantityUnit":"KILOGRAM"}
            """;
    private static final String BATCH = """
            {"id":0,"code":"LOT-2026-001","receptionId":%d,"registeredAt":"2026-10-03T12:00:00Z"}
            """;

    @Autowired
    ApplicationEvents events;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void registersAndReadsBatchLinkedToReception() throws Exception {
        var reception = mvc.perform(post(RECEPTIONS).contentType(MediaType.APPLICATION_JSON).content(RECEPTION))
                .andExpect(status().isCreated()).andReturn();
        var receptionId = createdId(reception, RECEPTIONS);
        var body = BATCH.formatted(receptionId);

        var batch = mvc.perform(post(BATCHES).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.receptionId").value(receptionId)).andReturn();
        var batchId = createdId(batch, BATCHES);
        assertTrue(batchId > 0);

        mvc.perform(get(BATCHES + "/{id}", batchId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("LOT-2026-001"));
        mvc.perform(get(BATCHES)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].receptionId").value(receptionId));
        mvc.perform(post(BATCHES).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());

        var event = events.stream(ProductionBatchRegistered.class).findFirst().orElseThrow();
        assertEquals(batchId, event.batchId().value());
        assertEquals(receptionId, event.receptionId().value());
        assertEquals(1, events.stream(ProductionBatchRegistered.class).count());
    }

    @Test
    void rejectsUnknownReceptionAndMissingFields() throws Exception {
        mvc.perform(post(BATCHES).contentType(MediaType.APPLICATION_JSON).content(BATCH.formatted(Long.MAX_VALUE)))
                .andExpect(status().isNotFound());
        mvc.perform(post(BATCHES).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get(BATCHES + "/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
        assertTrue(events.stream(ProductionBatchRegistered.class).findAny().isEmpty());
    }

    private long createdId(MvcResult result, String collection) {
        var path = URI.create(result.getResponse().getHeader("Location")).getPath();
        return Long.parseLong(path.substring(collection.length() + 1));
    }
}
