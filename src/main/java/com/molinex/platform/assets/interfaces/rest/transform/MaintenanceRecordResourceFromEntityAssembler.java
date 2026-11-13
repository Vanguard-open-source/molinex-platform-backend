package com.molinex.platform.assets.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.interfaces.rest.resources.MaintenanceRecordResource;

public final class MaintenanceRecordResourceFromEntityAssembler {
    private MaintenanceRecordResourceFromEntityAssembler() {
    }

    public static MaintenanceRecordResource toResourceFromEntity(MaintenanceRecord entity) {
        return new MaintenanceRecordResource(entity.getId().value(), entity.getMachineId().value(), entity.getType(),
                entity.getPerformedAt(), entity.getDescription().value(), entity.getResponsible().principalId(),
                entity.getResponsible().displayName(),
                entity.getAnomalyId() == null ? null : entity.getAnomalyId().value(),
                entity.getCorrectiveDetails() == null ? null : entity.getCorrectiveDetails().failure(),
                entity.getCorrectiveDetails() == null ? null : entity.getCorrectiveDetails().cause(),
                entity.getCorrectiveDetails() == null ? null : entity.getCorrectiveDetails().actionTaken(),
                entity.getCorrectiveDetails() == null ? null : entity.getCorrectiveDetails().downtimeMinutes());
    }
}
