package com.molinex.platform.assets.domain.model.commands;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;





public record RecordPreventiveMaintenanceCommand(
        Long machineId,
        Instant performedAt,
        String description,
        Long responsiblePrincipalId,
        String responsibleDisplayName
) {
}
