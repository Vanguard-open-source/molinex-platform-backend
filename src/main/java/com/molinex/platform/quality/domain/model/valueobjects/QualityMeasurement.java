package com.molinex.platform.quality.domain.model.valueobjects;

public record QualityMeasurement(
        QualityIndicator indicator,
        Percentage value,
        QualityRange expectedRange
) {
    public QualityMeasurement {
        if (indicator == null || value == null || expectedRange == null)
            throw new IllegalArgumentException("indicator, value and expected range are required");
    }

    public boolean isOutsideExpectedRange() {
        return !expectedRange.contains(value);
    }
}
