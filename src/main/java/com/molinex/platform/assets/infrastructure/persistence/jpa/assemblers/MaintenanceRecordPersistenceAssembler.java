package com.molinex.platform.assets.infrastructure.persistence.jpa.assemblers;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.infrastructure.persistence.jpa.entities.MaintenanceRecordPersistenceEntity;
import com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables
        .CorrectiveMaintenanceDetailsPersistenceEmbeddable;
import com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables.TechnicianReferencePersistenceEmbeddable;

public final class MaintenanceRecordPersistenceAssembler {
    private MaintenanceRecordPersistenceAssembler() {
    }

    public static MaintenanceRecord toDomainFromPersistence(MaintenanceRecordPersistenceEntity entity) {
        return new MaintenanceRecord(entity.getId() == null ? null : new MaintenanceRecordId(entity.getId()),
                entity.getMachineId(), entity.getType(), entity.getPerformedAt(), entity.getDescription(),
                entity.getResponsible() == null ? null : entity.getResponsible().toDomain(), entity.getAnomalyId(),
                entity.getCorrectiveDetails() == null ? null : entity.getCorrectiveDetails().toDomain());
    }

    public static MaintenanceRecordPersistenceEntity toPersistenceFromDomain(MaintenanceRecord aggregate) {
        var entity = new MaintenanceRecordPersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setMachineId(aggregate.getMachineId());
        entity.setType(aggregate.getType());
        entity.setPerformedAt(aggregate.getPerformedAt());
        entity.setDescription(aggregate.getDescription());
        entity.setResponsible(aggregate.getResponsible() == null ? null :
                TechnicianReferencePersistenceEmbeddable.fromDomain(aggregate.getResponsible()));
        entity.setAnomalyId(aggregate.getAnomalyId());
        entity.setCorrectiveDetails(aggregate.getCorrectiveDetails() == null ? null :
                CorrectiveMaintenanceDetailsPersistenceEmbeddable.fromDomain(aggregate.getCorrectiveDetails()));
        return entity;
    }
}
