package com.molinex.platform.commercial.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record PhoneNumber(
        String value
) {
    public PhoneNumber {
        if (value == null || value.isBlank() || value.length() > 30 || !value.matches("[+0-9() .-]+"))
            throw new IllegalArgumentException("PhoneNumber must be valid");
    }
}
