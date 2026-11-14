package com.molinex.platform.quality.domain.model.valueobjects;

public record ProductionRecordId(
        Long value
) {
    public ProductionRecordId {
        if (value == null || value <= 0) throw new IllegalArgumentException("ProductionRecordId must be valid");
    }
}
