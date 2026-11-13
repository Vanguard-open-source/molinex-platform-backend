package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record MachineName(
        String value
) {
    public MachineName {
        if (value != null && value.length() > 120)
            throw new IllegalArgumentException("value exceeds the storage limit of 120 characters");
        if (value == null || value.isBlank()) throw new IllegalArgumentException("MachineName must be valid");
    }
}
