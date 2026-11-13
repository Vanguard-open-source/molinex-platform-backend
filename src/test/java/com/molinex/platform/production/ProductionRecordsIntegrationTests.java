package com.molinex.platform.production;

import com.molinex.platform.production.interfaces.acl.ProductionContextFacade;
import com.molinex.platform.production.interfaces.events.ProductionInformationRecordedIntegrationEvent;
import com.molinex.platform.production.interfaces.events.ProductionInformationUpdatedIntegrationEvent;
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
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=${MOLINEX_FLOW_TEST_URL:jdbc:h2:mem:molinex-flow;MODE=MySQL;DB_CLOSE_DELAY=-1}",
        "spring.datasource.username=${MOLINEX_TEST_DB_USER:sa}",
        "spring.datasource.password=${MOLINEX_TEST_DB_PASSWORD:}",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
@RecordApplicationEvents
class ProductionRecordsIntegrationTests {

    private static final String RECEPTIONS = "/api/v1/raw-material-receptions";
    private static final String BATCHES = "/api/v1/production-batches";
    private static final String RECORDS = "/api/v1/production-records";
    private static final String RECORDED_AT = "2026-10-03T13:10:00Z";
    private static final String COMPLETED = """
            {"id":0,"batchId":%d,"processName":"Cleaning and husking","processedWeightValue":8200,
             "processedWeightUnit":"KILOGRAM","startedAt":"2026-10-03T13:15:00Z","finishedAt":"2026-10-03T14:40:00Z",
             "status":"COMPLETED","recordedAt":"%s"}
            """;

    @Autowired
    ProductionContextFacade facade;

    @Autowired
    ApplicationEvents events;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void connectsReceptionBatchAndProcessAndQueriesHistory() throws Exception {
        long batch = createBatch();
        long anotherBatch = createBatch();
        long record = create(RECORDS, COMPLETED.formatted(batch, RECORDED_AT));
        create(RECORDS, COMPLETED.formatted(anotherBatch, "2026-09-25T13:10:00Z"));

        mvc.perform(get(RECORDS + "/{id}", record)).andExpect(status().isOk())
                .andExpect(jsonPath("$.batchId").value(batch))
                .andExpect(jsonPath("$.recordedAt").value(RECORDED_AT));
        mvc.perform(get(RECORDS).param("batchId", Long.toString(batch))).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(record));
        mvc.perform(get(RECORDS).param("batchId", Long.toString(Long.MAX_VALUE))).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get(RECORDS + "/history").param("from", RECORDED_AT).param("to", RECORDED_AT))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(record));
        mvc.perform(get(RECORDS + "/history").param("to", "2020-01-01T00:00:00Z"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get(RECORDS + "/history").param("from", RECORDED_AT).param("to", "2020-01-01T00:00:00Z"))
                .andExpect(status().isBadRequest());

        assertTrue(facade.batchExists(batch));
        assertFalse(facade.batchExists(Long.MAX_VALUE));
        assertEquals(Instant.parse(RECORDED_AT), facade.findRecord(record).orElseThrow().recordedAt());
        assertEquals(record, facade.findRecords(batch).getFirst().id());
        assertTrue(events.stream(ProductionInformationRecordedIntegrationEvent.class)
                .anyMatch(event -> event.productionRecordId().equals(record)));
    }

    @Test
    void updatesDetailsAndPreservesOriginalBatchAndDate() throws Exception {
        long batch = createBatch();
        long anotherBatch = createBatch();
        long record = create(RECORDS, COMPLETED.formatted(batch, "2026-10-04T13:10:00Z"));
        String update = COMPLETED.formatted(batch, "2026-10-05T13:10:00Z")
                .replace("Cleaning and husking", "Polishing").replace("8200", "8100");

        mvc.perform(put(RECORDS + "/{id}", record).contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isOk()).andExpect(jsonPath("$.recordedAt").value("2026-10-04T13:10:00Z"));
        mvc.perform(put(RECORDS + "/{id}", record).contentType(MediaType.APPLICATION_JSON)
                        .content(update.replace("\"batchId\":" + batch, "\"batchId\":" + anotherBatch)))
                .andExpect(status().isBadRequest());
        mvc.perform(put(RECORDS + "/{id}", record).contentType(MediaType.APPLICATION_JSON)
                        .content(update.replace("8100", "0")))
                .andExpect(status().isBadRequest());
        mvc.perform(get(RECORDS + "/{id}", record)).andExpect(status().isOk())
                .andExpect(jsonPath("$.batchId").value(batch))
                .andExpect(jsonPath("$.processName").value("Polishing"))
                .andExpect(jsonPath("$.processedWeightValue").value(8100));
        mvc.perform(put(RECORDS + "/{id}", Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isNotFound());
        assertEquals(1, events.stream(ProductionInformationUpdatedIntegrationEvent.class).count());
    }

    @Test
    void rejectsInvalidRegistrationAndUnknownBatch() throws Exception {
        long batch = createBatch();
        String valid = COMPLETED.formatted(batch, RECORDED_AT);
        for (var invalid : new String[]{
                "{}", valid.replace("8200", "-1"), valid.replace("KILOGRAM", "POUND"),
                valid.replace("2026-10-03T14:40:00Z", "2026-10-03T12:40:00Z"),
                valid.replace("\"2026-10-03T14:40:00Z\"", "null")
        }) {
            mvc.perform(post(RECORDS).contentType(MediaType.APPLICATION_JSON).content(invalid))
                    .andExpect(status().isBadRequest());
        }
        mvc.perform(post(RECORDS).contentType(MediaType.APPLICATION_JSON)
                        .content(COMPLETED.formatted(Long.MAX_VALUE, RECORDED_AT)))
                .andExpect(status().isNotFound());
        mvc.perform(get(RECORDS).param("batchId", Long.toString(batch))).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        assertTrue(events.stream(ProductionInformationRecordedIntegrationEvent.class).findAny().isEmpty());
    }

    private long createBatch() throws Exception {
        long reception = create(RECEPTIONS, """
                {"receivedAt":"2026-10-03T11:30:00Z","supplierName":"Agro Norte",
                 "originDescription":"Lambayeque","quantityValue":18000,"quantityUnit":"KILOGRAM"}
                """);
        return create(BATCHES, """
                {"code":"LOT-%s","receptionId":%d,"registeredAt":"2026-10-03T12:00:00Z"}
                """.formatted(UUID.randomUUID(), reception));
    }

    private long create(String path, String body) throws Exception {
        var response = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn().getResponse();
        var location = URI.create(response.getHeader("Location")).getPath();
        return Long.parseLong(location.substring(path.length() + 1));
    }
}
