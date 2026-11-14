package com.molinex.platform.quality.domain.model.valueobjects;

public record QualityAssessmentId(
        Integer value
) {
    public QualityAssessmentId {
        if (value == null || value <= 0) throw new IllegalArgumentException("QualityAssessmentId must be valid");
    }
}
