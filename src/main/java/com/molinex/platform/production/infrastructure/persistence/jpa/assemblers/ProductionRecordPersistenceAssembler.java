package com.molinex.platform.production.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionDetails;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.production.infrastructure.persistence.jpa.entities.ProductionRecordPersistenceEntity;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;

public final class ProductionRecordPersistenceAssembler {

    private ProductionRecordPersistenceAssembler() {
    }

    public static void setDetails(ProductionRecordPersistenceEntity entity, ProductionDetails details) {
        entity.updateDetails(details.processName(), details.processedWeight().value(), details.processedWeight().unit(),
                details.startedAt(), details.finishedAt(), details.status());
    }

    public static ProductionRecord toDomain(ProductionRecordPersistenceEntity entity) {
        var details = new ProductionDetails(entity.getProcessName(),
                new Weight(entity.getProcessedWeightValue(), entity.getProcessedWeightUnit()),
                entity.getStartedAt(), entity.getFinishedAt(), entity.getStatus());
        return new ProductionRecord(new ProductionRecordId(entity.getId()), new ProductionBatchId(entity.getBatchId()),
                details, entity.getRecordedAt());
    }
}
