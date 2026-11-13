package com.molinex.platform.assets.infrastructure.persistence.jpa.assemblers;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.infrastructure.persistence.jpa.entities.MachinePersistenceEntity;


public final class MachinePersistenceAssembler {
    private MachinePersistenceAssembler() {
    }

    public static Machine toDomainFromPersistence(MachinePersistenceEntity entity) {
        return new Machine(entity.getId() == null ? null : new MachineId(entity.getId()), entity.getCode(),
                entity.getName(), entity.getModel(), entity.getStatus(), entity.getStatusChangedAt());
    }

    public static MachinePersistenceEntity toPersistenceFromDomain(Machine aggregate) {
        var entity = new MachinePersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setCode(aggregate.getCode());
        entity.setName(aggregate.getName());
        entity.setModel(aggregate.getModel());
        entity.setStatus(aggregate.getStatus());
        entity.setStatusChangedAt(aggregate.getStatusChangedAt());
        return entity;
    }
}
