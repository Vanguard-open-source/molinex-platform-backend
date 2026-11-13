package com.molinex.platform.production.domain.model.valueobjects;

public record BatchCode(String value) {

    public BatchCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Batch code is required");
        }
    }
}
