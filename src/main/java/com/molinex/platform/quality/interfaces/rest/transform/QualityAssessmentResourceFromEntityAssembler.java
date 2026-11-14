package com.molinex.platform.quality.interfaces.rest.transform;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.interfaces.rest.resources.QualityAssessmentResource;
import com.molinex.platform.quality.interfaces.rest.resources.QualityMeasurementResource;

public final class QualityAssessmentResourceFromEntityAssembler {
    private QualityAssessmentResourceFromEntityAssembler() {
    }

    public static QualityAssessmentResource toResourceFromEntity(QualityAssessment entity) {
        return new QualityAssessmentResource(entity.getId().value(), entity.getProductionRecordId().value(),
                entity.getMeasurements().stream()
                        .map(m -> new QualityMeasurementResource(m.indicator(), m.value().value(),
                                m.expectedRange().minimum().value(), m.expectedRange().maximum().value())).toList(),
                entity.getAssessedAt());
    }
}
