package com.molinex.platform.iam.domain.model.valueobjects;

public record UserProfile(
        String fullName
) {
    public UserProfile {
        if (fullName != null && fullName.length() > 150)
            throw new IllegalArgumentException("fullName exceeds the storage limit of 150 characters");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("UserProfile must be valid");
    }
}
