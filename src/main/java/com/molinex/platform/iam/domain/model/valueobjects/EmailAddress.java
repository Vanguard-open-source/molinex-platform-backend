package com.molinex.platform.iam.domain.model.valueobjects;

public record EmailAddress(
        String value
) {
    public EmailAddress {
        if (value != null && value.length() > 254)
            throw new IllegalArgumentException("value exceeds the storage limit of 254 characters");
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new IllegalArgumentException("EmailAddress must be valid");
    }
}
