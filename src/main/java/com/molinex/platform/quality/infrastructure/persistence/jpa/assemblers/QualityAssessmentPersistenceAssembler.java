package com.molinex.platform.quality.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;
import com.molinex.platform.quality.infrastructure.persistence.jpa.entities.QualityAssessmentPersistenceEntity;
import com.molinex.platform.quality.infrastructure.persistence.jpa.entities.QualityMeasurementPersistenceEntity;


public final class QualityAssessmentPersistenceAssembler {
    private QualityAssessmentPersistenceAssembler() {
    }

    public static QualityAssessment toDomainFromPersistence(QualityAssessmentPersistenceEntity entity) {
        return new QualityAssessment(entity.getId() == null ? null : new QualityAssessmentId(entity.getId()),
                entity.getProductionRecordId(),
                entity.getMeasurements().stream().map(QualityMeasurementPersistenceEntity::toDomain).toList(),
                entity.getAssessedAt());
    }

    public static QualityAssessmentPersistenceEntity toPersistenceFromDomain(QualityAssessment aggregate) {
        var entity = new QualityAssessmentPersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setProductionRecordId(aggregate.getProductionRecordId());
        var measurements = aggregate.getMeasurements().stream().map(QualityMeasurementPersistenceEntity::fromDomain)
                .toList();
        measurements.forEach(measurement -> measurement.setAssessment(entity));
        entity.setMeasurements(measurements);
        entity.setAssessedAt(aggregate.getAssessedAt());
        return entity;
    }
}
