package com.molinex.platform.quality.domain.model.valueobjects;

public record QualityRange(
        Percentage minimum,
        Percentage maximum
) {
    public QualityRange {
        if (minimum == null || maximum == null || minimum.value().compareTo(maximum.value()) > 0)
            throw new IllegalArgumentException("Expected range must have minimum <= maximum");
    }

    public boolean contains(Percentage value) {
        return value.value().compareTo(minimum.value()) >= 0 && value.value().compareTo(maximum.value()) <= 0;
    }
}
