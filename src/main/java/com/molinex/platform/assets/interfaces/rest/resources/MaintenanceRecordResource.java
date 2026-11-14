package com.molinex.platform.assets.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;




public record MaintenanceRecordResource(
        Long id,
        Long machineId,
        MaintenanceType type,
        Instant performedAt,
        String description,
        Long responsiblePrincipalId,
        String responsibleDisplayName,
        Long anomalyId,
        String failure,
        String cause,
        String actionTaken,
        Integer downtimeMinutes
) {
}
