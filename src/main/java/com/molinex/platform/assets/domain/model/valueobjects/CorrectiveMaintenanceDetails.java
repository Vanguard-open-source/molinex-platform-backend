package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record CorrectiveMaintenanceDetails(
        String failure,
        String cause,
        String actionTaken,
        Integer downtimeMinutes
) {
    public CorrectiveMaintenanceDetails {
        if (actionTaken != null && actionTaken.length() > 2000)
            throw new IllegalArgumentException("actionTaken exceeds the storage limit of 2000 characters");
        if (cause != null && cause.length() > 2000)
            throw new IllegalArgumentException("cause exceeds the storage limit of 2000 characters");
        if (failure != null && failure.length() > 2000)
            throw new IllegalArgumentException("failure exceeds the storage limit of 2000 characters");
        if (failure == null || failure.isBlank() || actionTaken == null || actionTaken.isBlank())
            throw new IllegalArgumentException("failure and actionTaken are required for corrective maintenance");
        if (downtimeMinutes != null && downtimeMinutes < 0)
            throw new IllegalArgumentException("downtimeMinutes must be nonnegative");
    }
}
