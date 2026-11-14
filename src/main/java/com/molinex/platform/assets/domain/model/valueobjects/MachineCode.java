package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record MachineCode(
        String value
) {
    public MachineCode {
        if (value != null && value.length() > 50)
            throw new IllegalArgumentException("value exceeds the storage limit of 50 characters");
        if (value == null || value.isBlank()) throw new IllegalArgumentException("MachineCode must be valid");
    }
}
