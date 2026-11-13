package com.molinex.platform.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.molinex.platform.assets.domain.model.valueobjects.CorrectiveMaintenanceDetails;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceDescription;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;
import com.molinex.platform.assets.domain.model.valueobjects.TechnicianReference;

import com.molinex.platform.assets.domain.model.queries.GetMachineMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;

import com.molinex.platform.assets.application.internal.queryservices.MaintenanceRecordQueryServiceImpl;

class MaintenanceHistoryScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    MaintenanceRecordRepository repository = mock(MaintenanceRecordRepository.class);
    MachineRepository machines = mock(MachineRepository.class);
    MaintenanceRecordQueryServiceImpl service = new MaintenanceRecordQueryServiceImpl(repository, machines);
    Long id = 1L;

    @BeforeEach
    void setup() {
        when(machines.findById(any())).thenReturn(Optional.of(
                new Machine(new MachineId(id), new MachineCode("M-1"), new MachineName("Husker"),
                        new MachineModel("H-200"), MachineStatus.OPERATIONAL, TIME)));
    }

    MaintenanceRecord record(Long machine, MaintenanceType type) {
        return new MaintenanceRecord(new MaintenanceRecordId(1L), new MachineId(machine), type, TIME,
                new MaintenanceDescription("Inspection"), new TechnicianReference(2L, "Ana"), null,
                type == MaintenanceType.CORRECTIVE ? new CorrectiveMaintenanceDetails("Failure", null, "Repair", 1) :
                        null);
    }

    @Test
    @DisplayName("US-21 E1: History with interventions")
    void us21Scenario1() throws Exception {
        when(repository.findAll()).thenReturn(
                List.of(record(id, MaintenanceType.PREVENTIVE), record(id, MaintenanceType.CORRECTIVE),
                        record(2L, MaintenanceType.PREVENTIVE)));
        var values = service.handle(new GetMachineMaintenanceHistoryQuery(id)).value();
        assertEquals(2, values.size());
        assertTrue(values.stream().allMatch(v -> v.getMachineId().value().equals(id)));
        assertEquals(2, values.stream().map(MaintenanceRecord::getType).distinct().count());
    }

    @Test
    @DisplayName("US-21 E2: Machine without maintenance")
    void us21Scenario2() throws Exception {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.handle(new GetMachineMaintenanceHistoryQuery(id)).value().isEmpty());
    }

    @Test
    @DisplayName("US-21 E3: Missing machine")
    void us21Scenario3() throws Exception {
        when(machines.findById(any())).thenReturn(Optional.empty());
        var result = service.handle(new GetMachineMaintenanceHistoryQuery(id));
        assertFalse(result.isSuccess());
        verify(repository, never()).findAll();
    }

}
