package com.molinex.platform.assets;

import com.molinex.platform.assets.application.commandservices.MachineCommandService;
import com.molinex.platform.assets.interfaces.rest.MachinesController;
import com.molinex.platform.assets.interfaces.rest.resources.MachineStatusResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Optional;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;

import com.molinex.platform.assets.domain.model.queries.GetMachineStatusQuery;
import com.molinex.platform.assets.domain.repositories.MachineRepository;

import com.molinex.platform.assets.application.internal.queryservices.MachineQueryServiceImpl;

class MachineStatusScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    MachineRepository repository = mock(MachineRepository.class);
    MachineQueryServiceImpl service = new MachineQueryServiceImpl(repository);
    Long id = 1L;

    Machine machine(Instant changed) {
        return new Machine(new MachineId(id), new MachineCode("M-1"), new MachineName("Husker"),
                new MachineModel("H-200"), MachineStatus.OPERATIONAL, changed);
    }

    @Test
    @DisplayName("US-18 E1: State of an existing machine")
    void us18Scenario1() throws Exception {
        when(repository.findById(new MachineId(id))).thenReturn(Optional.of(machine(TIME)));
        var result = service.handle(new GetMachineStatusQuery(id)).orElseThrow();
        assertEquals(MachineStatus.OPERATIONAL, result.getStatus());
        assertEquals(TIME, result.getStatusChangedAt());
        assertEquals(id, result.getId().value());
    }

    @Test
    @DisplayName("US-18 E2: Missing machine")
    void us18Scenario2() throws Exception {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertTrue(service.handle(new GetMachineStatusQuery(id)).isEmpty());
    }

    @Test
    @DisplayName("US-18 E3: Machine state pending update")
    void us18Scenario3() throws Exception {
        when(repository.findById(any())).thenReturn(Optional.of(machine(null)));
        var controller = new MachinesController(mock(MachineCommandService.class), service);
        var response = controller.status(id);
        var body = (MachineStatusResource) response.getBody();
        assertNotNull(body);
        assertTrue(body.statusUpdatePending());
        assertNull(body.statusChangedAt());
    }

}
