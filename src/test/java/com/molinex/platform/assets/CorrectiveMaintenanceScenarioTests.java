package com.molinex.platform.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Optional;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;
import com.molinex.platform.assets.domain.model.commands.RecordCorrectiveMaintenanceCommand;

import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;
import com.molinex.platform.assets.application.internal.commandservices.MaintenanceRecordCommandServiceImpl;


class CorrectiveMaintenanceScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    MaintenanceRecordRepository repository = mock(MaintenanceRecordRepository.class);
    MachineRepository machines = mock(MachineRepository.class);
    MaintenanceRecordCommandServiceImpl service = new MaintenanceRecordCommandServiceImpl(repository, machines);
    Long id = 1L;
    Long technician = 2L;

    @BeforeEach
    void setup() {
        when(machines.findById(any())).thenReturn(Optional.of(
                new Machine(new MachineId(id), new MachineCode("M-1"), new MachineName("Husker"),
                        new MachineModel("H-200"), MachineStatus.OPERATIONAL, TIME)));
        when(repository.save(any())).thenAnswer(i -> {
            MaintenanceRecord a = i.getArgument(0);
            a.assignId(new MaintenanceRecordId(1L));
            return a;
        });
    }

    @Test
    @DisplayName("US-20 E1: Valid corrective maintenance")
    void us20Scenario1() throws Exception {
        var result = service.handle(
                new RecordCorrectiveMaintenanceCommand(id, TIME, "Replace belt", technician, "Ana", null,
                        "Belt slipped", "Wear", "Replace belt", 75));
        assertTrue(result.isSuccess());
        var value = result.value();
        assertEquals(MaintenanceType.CORRECTIVE, value.getType());
        assertEquals("Belt slipped", value.getCorrectiveDetails().failure());
        assertNull(value.getAnomalyId());
    }

    @Test
    @DisplayName("US-20 E2: Missing machine")
    void us20Scenario2() throws Exception {
        when(machines.findById(any())).thenReturn(Optional.empty());
        assertFalse(service.handle(
                new RecordCorrectiveMaintenanceCommand(id, TIME, "Replace belt", technician, "Ana", null,
                        "Belt slipped", "Wear", "Replace belt", 75)).isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("US-20 E3: Incomplete intervention data")
    void us20Scenario3() throws Exception {
        assertFalse(service.handle(
                new RecordCorrectiveMaintenanceCommand(id, TIME, "Replace belt", technician, "Ana", null, null, null,
                        "Replace belt", 75)).isSuccess());
        assertFalse(service.handle(
                new RecordCorrectiveMaintenanceCommand(id, TIME, "Replace belt", technician, "Ana", null,
                        "Belt slipped", null, null, 75)).isSuccess());
        verify(repository, never()).save(any());
    }

}
