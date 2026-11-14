package com.molinex.platform.production.domain.model.valueobjects;

public record ProductionRecordId(Long value) {

    public ProductionRecordId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ProductionRecordId must be positive");
        }
    }
}
