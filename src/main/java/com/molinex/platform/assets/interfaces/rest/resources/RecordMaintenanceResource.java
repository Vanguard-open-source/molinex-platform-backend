package com.molinex.platform.assets.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RecordMaintenanceResource(
        @NotNull
        Long machineId,
        @NotNull
        MaintenanceType type,
        @NotNull
        Instant performedAt,
        @NotBlank
        String description,
        @NotNull
        Long responsiblePrincipalId,
        @NotBlank
        String responsibleDisplayName,
        Long anomalyId,
        String failure,
        String cause,
        String actionTaken,
        @PositiveOrZero
        Integer downtimeMinutes
) {
}
