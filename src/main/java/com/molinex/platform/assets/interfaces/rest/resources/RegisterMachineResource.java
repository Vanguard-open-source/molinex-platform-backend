package com.molinex.platform.assets.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterMachineResource(
        @NotBlank
        String code,
        @NotBlank
        String name,
        @NotBlank
        String model,
        @NotNull
        MachineStatus status,
        @NotNull
        Instant statusChangedAt
) {
}
