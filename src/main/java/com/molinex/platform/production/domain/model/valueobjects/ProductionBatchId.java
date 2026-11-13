package com.molinex.platform.production.domain.model.valueobjects;

public record ProductionBatchId(Long value) {

    public ProductionBatchId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ProductionBatchId must be positive");
        }
    }
}
