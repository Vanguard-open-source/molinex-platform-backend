package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record MaintenanceDescription(
        String value
) {
    public MaintenanceDescription {
        if (value != null && value.length() > 2000)
            throw new IllegalArgumentException("value exceeds the storage limit of 2000 characters");
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("MaintenanceDescription must be valid");
    }
}
