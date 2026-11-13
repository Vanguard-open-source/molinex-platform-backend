package com.molinex.platform.production.domain.model.valueobjects;

public record RawMaterialReceptionId(Long value) {

    public RawMaterialReceptionId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("RawMaterialReceptionId must be positive");
        }
    }
}
