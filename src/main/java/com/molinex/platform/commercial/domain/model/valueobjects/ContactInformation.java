package com.molinex.platform.commercial.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record ContactInformation(
        String fullName,
        EmailAddress email,
        PhoneNumber phoneNumber,
        String companyName
) {
    public ContactInformation {
        if (fullName == null || fullName.isBlank() || fullName.length() > 150)
            throw new IllegalArgumentException("fullName is required and must fit 150 characters");
        if (companyName == null || companyName.isBlank() || companyName.length() > 150)
            throw new IllegalArgumentException("companyName is required and must fit 150 characters");
        if (email == null) throw new IllegalArgumentException("email is required");
    }
}
