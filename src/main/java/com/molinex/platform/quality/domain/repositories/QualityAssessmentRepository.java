package com.molinex.platform.quality.domain.repositories;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;

import java.util.List;
import java.util.Optional;

public interface QualityAssessmentRepository {
    QualityAssessment save(QualityAssessment aggregate);

    Optional<QualityAssessment> findById(QualityAssessmentId id);

    List<QualityAssessment> findAll();
}
