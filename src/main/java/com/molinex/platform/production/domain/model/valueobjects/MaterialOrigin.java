package com.molinex.platform.production.domain.model.valueobjects;

public record MaterialOrigin(String description) {

    public MaterialOrigin {
        if (description == null || description.isBlank() || description.length() > 255) {
            throw new IllegalArgumentException("Origin is required and must have at most 255 characters");
        }
    }
}
