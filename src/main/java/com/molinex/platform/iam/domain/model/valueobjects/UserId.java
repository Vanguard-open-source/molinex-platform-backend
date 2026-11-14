package com.molinex.platform.iam.domain.model.valueobjects;

public record UserId(
        Integer value
) {
    public UserId {
        if (value == null || value <= 0) throw new IllegalArgumentException("UserId must be valid");
    }
}
