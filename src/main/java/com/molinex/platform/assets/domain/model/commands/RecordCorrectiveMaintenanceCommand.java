package com.molinex.platform.assets.domain.model.commands;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;





public record RecordCorrectiveMaintenanceCommand(
        Long machineId,
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
