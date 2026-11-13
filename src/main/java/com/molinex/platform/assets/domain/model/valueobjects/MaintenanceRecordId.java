package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record MaintenanceRecordId(
        Long value
) {
    public MaintenanceRecordId {
        if (value == null || value <= 0) throw new IllegalArgumentException("MaintenanceRecordId must be valid");
    }
}
