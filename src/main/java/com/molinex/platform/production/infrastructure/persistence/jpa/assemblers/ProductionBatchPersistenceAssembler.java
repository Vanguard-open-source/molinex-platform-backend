package com.molinex.platform.production.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.BatchRegistrationDateTime;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.infrastructure.persistence.jpa.entities.ProductionBatchPersistenceEntity;
import com.molinex.platform.production.infrastructure.persistence.jpa.entities.RawMaterialReceptionPersistenceEntity;

public final class ProductionBatchPersistenceAssembler {

    private ProductionBatchPersistenceAssembler() {
    }

    public static ProductionBatchPersistenceEntity toEntity(ProductionBatch batch,
                                                             RawMaterialReceptionPersistenceEntity reception) {
        return new ProductionBatchPersistenceEntity(batch.code().value(), reception, batch.registeredAt().value());
    }

    public static ProductionBatch toDomain(ProductionBatchPersistenceEntity entity) {
        var batch = ProductionBatch.register(new BatchCode(entity.getCode()),
                new RawMaterialReceptionId(entity.getReceptionId()), new BatchRegistrationDateTime(entity.getRegisteredAt()));
        batch.assignId(new ProductionBatchId(entity.getId()));
        return batch;
    }
}
