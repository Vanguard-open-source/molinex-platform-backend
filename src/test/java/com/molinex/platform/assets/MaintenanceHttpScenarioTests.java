package com.molinex.platform.assets;

import com.molinex.platform.assets.interfaces.rest.MaintenanceRecordsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;
import com.molinex.platform.assets.application.internal.commandservices.MaintenanceRecordCommandServiceImpl;
import com.molinex.platform.assets.application.internal.queryservices.MaintenanceRecordQueryServiceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.molinex.platform.shared.interfaces.rest.GlobalExceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.any;

import org.springframework.http.MediaType;

class MaintenanceHttpScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    MaintenanceRecordRepository records = mock(MaintenanceRecordRepository.class);
    MachineRepository machines = mock(MachineRepository.class);
    MockMvc mvc;
    Long id = 1L;
    static final String BODY =
            "{\"machineId\":1,\"type\":\"PREVENTIVE\",\"performedAt\":\"2026-10-03T10:00:00Z\",\"description\":\"Inspection\",\"responsiblePrincipalId\":2,\"responsibleDisplayName\":\"Ana\",\"anomalyId\":null,\"failure\":null,\"cause\":null,\"actionTaken\":null,\"downtimeMinutes\":null}";

    @BeforeEach
    void setup() {
        when(machines.findById(new MachineId(id))).thenReturn(Optional.of(
                new Machine(new MachineId(id), new MachineCode("M-1"), new MachineName("Husker"),
                        new MachineModel("H-200"), MachineStatus.OPERATIONAL, TIME)));
        when(records.save(any())).thenAnswer(i -> {
            MaintenanceRecord a = i.getArgument(0);
            a.assignId(new MaintenanceRecordId(1L));
            return a;
        });
        when(records.findAll()).thenReturn(List.of());
        mvc = MockMvcBuilders.standaloneSetup(
                        new MaintenanceRecordsController(new MaintenanceRecordCommandServiceImpl(records, machines),
                                new MaintenanceRecordQueryServiceImpl(records, machines)))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    @DisplayName("TS-03 E1: Valid maintenance creation")
    void ts03Scenario1() throws Exception {
        mvc.perform(post("/api/v1/maintenance-records").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.machineId").value(id))
                .andExpect(jsonPath("$.type").value("PREVENTIVE")).andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("TS-03 E2: Query maintenance of an existing machine")
    void ts03Scenario2() throws Exception {
        mvc.perform(get("/api/v1/maintenance-records").param("machineId", id.toString())).andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("TS-03 E3: Invalid maintenance request")
    void ts03Scenario3() throws Exception {
        mvc.perform(post("/api/v1/maintenance-records").contentType(MediaType.APPLICATION_JSON)
                .content(BODY.replace("PREVENTIVE", "INVALID"))).andExpect(status().isBadRequest());
        mvc.perform(post("/api/v1/maintenance-records").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verify(records, never()).save(any());
    }

    @Test
    @DisplayName("TS-03 E4: Missing machine")
    void ts03Scenario4() throws Exception {
        when(machines.findById(any())).thenReturn(Optional.empty());
        mvc.perform(post("/api/v1/maintenance-records").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/v1/maintenance-records").param("machineId", id.toString()))
                .andExpect(status().isNotFound());
        verify(records, never()).save(any());
    }

    @Test
    @DisplayName("TS-03 E5: Missing maintenance intervention")
    void ts03Scenario5() throws Exception {
        mvc.perform(get("/api/v1/maintenance-records/99")).andExpect(status().isNotFound());
    }

}
