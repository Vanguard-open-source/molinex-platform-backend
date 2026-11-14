package com.molinex.platform.quality.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;
import com.molinex.platform.quality.infrastructure.persistence.jpa.embeddables.WeightPersistenceEmbeddable;
import com.molinex.platform.quality.infrastructure.persistence.jpa.entities.WasteRecordPersistenceEntity;

public final class WasteRecordPersistenceAssembler {
    private WasteRecordPersistenceAssembler() {
    }

    public static WasteRecord toDomainFromPersistence(WasteRecordPersistenceEntity entity) {
        return new WasteRecord(entity.getId() == null ? null : new WasteRecordId(entity.getId()),
                entity.getProductionRecordId(), entity.getQuantity() == null ? null : entity.getQuantity().toDomain(),
                entity.getBaseWeight() == null ? null : entity.getBaseWeight().toDomain(), entity.getPercentage(),
                entity.getRecordedAt());
    }

    public static WasteRecordPersistenceEntity toPersistenceFromDomain(WasteRecord aggregate) {
        var entity = new WasteRecordPersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setProductionRecordId(aggregate.getProductionRecordId());
        entity.setQuantity(aggregate.getQuantity() == null ? null :
                WeightPersistenceEmbeddable.fromDomain(aggregate.getQuantity()));
        entity.setBaseWeight(aggregate.getBaseWeight() == null ? null :
                WeightPersistenceEmbeddable.fromDomain(aggregate.getBaseWeight()));
        entity.setPercentage(aggregate.getPercentage());
        entity.setRecordedAt(aggregate.getRecordedAt());
        return entity;
    }
}
