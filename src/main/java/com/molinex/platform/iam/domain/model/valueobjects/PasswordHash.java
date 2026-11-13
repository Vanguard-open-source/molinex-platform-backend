package com.molinex.platform.iam.domain.model.valueobjects;

public record PasswordHash(
        String value
) {
    public PasswordHash {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("PasswordHash must be valid");
    }
}
