package com.molinex.platform.assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.math.BigDecimal;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;
import com.molinex.platform.assets.domain.model.commands.RegisterMachineCommand;

import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.application.internal.commandservices.MachineCommandServiceImpl;


class MachineRegistrationScenarioTests {
    static final Instant TIME = Instant.parse("2026-10-03T10:00:00Z");
    MachineRepository repository = mock(MachineRepository.class);
    MachineCommandServiceImpl service = new MachineCommandServiceImpl(repository);

    @BeforeEach
    void setup() {
        when(repository.save(any())).thenAnswer(i -> {
            Machine a = i.getArgument(0);
            a.assignId(new MachineId(1L));
            return a;
        });
    }

    @Test
    @DisplayName("US-17 E1: Valid machine registration")
    void us17Scenario1() throws Exception {
        var result = service.handle(
                new RegisterMachineCommand("M-1", "Husker", "H-200", MachineStatus.OPERATIONAL, TIME));
        assertTrue(result.isSuccess());
        assertNotNull(result.value().getId());
        assertEquals(MachineStatus.OPERATIONAL, result.value().getStatus());
    }

    @Test
    @DisplayName("US-17 E2: Duplicate code")
    void us17Scenario2() throws Exception {
        when(repository.existsByCode(any())).thenReturn(true);
        assertFalse(service.handle(new RegisterMachineCommand("M-1", "Husker", "H-200", MachineStatus.OPERATIONAL, TIME))
                .isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("US-17 E3: Incomplete required machine data")
    void us17Scenario3() throws Exception {
        assertFalse(service.handle(new RegisterMachineCommand("M-1", "", "H-200", MachineStatus.OPERATIONAL, TIME))
                .isSuccess());
        assertFalse(service.handle(new RegisterMachineCommand("M-1", "Husker", "", MachineStatus.OPERATIONAL, TIME))
                .isSuccess());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("US-17 E4: Unsupported machine status")
    void us17Scenario4() throws Exception {
        assertFalse(service.handle(new RegisterMachineCommand("M-1", "Husker", "H-200", null, TIME)).isSuccess());
        verify(repository, never()).save(any());
    }

}
