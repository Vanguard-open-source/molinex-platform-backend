package com.molinex.platform.assets.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;




public record MachineStatusResource(
        Long id,
        MachineStatus status,
        Instant statusChangedAt,
        boolean statusUpdatePending
) {
}
