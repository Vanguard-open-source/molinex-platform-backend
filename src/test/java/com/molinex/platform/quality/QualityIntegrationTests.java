package com.molinex.platform.quality;

import com.molinex.platform.production.interfaces.acl.ProductionContextFacade;
import com.molinex.platform.production.interfaces.acl.ProductionRecordSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:quality-tests;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
class QualityIntegrationTests {
    private static final long RECORD = 3_000_000_001L;
    private static final long BATCH = 3_000_000_002L;
    private static final Instant OBSERVED = Instant.parse("2026-10-03T13:10:00Z");

    @MockitoBean
    ProductionContextFacade production;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void persistsQualityAndWasteAndQueriesByProductionReference() throws Exception {
        var snapshot = new ProductionRecordSnapshot(RECORD, BATCH, OBSERVED);
        when(production.findRecord(RECORD)).thenReturn(Optional.of(snapshot));
        when(production.batchExists(BATCH)).thenReturn(true);
        when(production.findRecords(BATCH)).thenReturn(List.of(snapshot));
        when(production.batchExists(BATCH + 1)).thenReturn(true);

        mvc.perform(post("/api/v1/quality-results").contentType(MediaType.APPLICATION_JSON).content("""
                {"productionRecordId":%d,"assessedAt":"%s","measurements":[
                 {"indicator":"WHOLE_GRAIN_PERCENTAGE","value":80,"expectedMinimum":75,"expectedMaximum":100},
                 {"indicator":"BROKEN_GRAIN_PERCENTAGE","value":20,"expectedMinimum":0,"expectedMaximum":25},
                 {"indicator":"YIELD_PERCENTAGE","value":75,"expectedMinimum":70,"expectedMaximum":100}]}
                """.formatted(RECORD, OBSERVED)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productionRecordId").value(RECORD));
        mvc.perform(post("/api/v1/waste-records").contentType(MediaType.APPLICATION_JSON).content("""
                {"productionRecordId":%d,"recordedAt":"%s","quantityValue":50,"quantityUnit":"KILOGRAM",
                 "baseWeightValue":1,"baseWeightUnit":"METRIC_TON"}
                """.formatted(RECORD, OBSERVED)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.percentage").value(5));
        mvc.perform(get("/api/v1/yield-indicators").param("productionRecordId", Long.toString(RECORD))
                        .param("from", OBSERVED.toString()).param("to", OBSERVED.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.indicators[?(@.indicator == 'WASTE_PERCENTAGE')].value").isNotEmpty());
        mvc.perform(get("/api/v1/rice-composition").param("batchId", Long.toString(BATCH)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.assessments[0].measurements.length()").value(3));
        mvc.perform(get("/api/v1/rice-composition").param("batchId", Long.toString(BATCH + 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
}
