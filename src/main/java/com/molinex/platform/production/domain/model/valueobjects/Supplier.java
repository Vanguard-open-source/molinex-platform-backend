package com.molinex.platform.production.domain.model.valueobjects;

public record Supplier(String name) {

    public Supplier {
        if (name == null || name.isBlank() || name.length() > 150) {
            throw new IllegalArgumentException("Supplier name is required and must have at most 150 characters");
        }
    }
}
