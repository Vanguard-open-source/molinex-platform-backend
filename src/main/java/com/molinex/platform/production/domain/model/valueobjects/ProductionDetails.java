package com.molinex.platform.production.domain.model.valueobjects;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;

import java.time.Instant;

public record ProductionDetails(
        String processName, Weight processedWeight, Instant startedAt, Instant finishedAt, ProductionStatus status
) {
    public ProductionDetails {
        if (processName == null || processName.isBlank() || processName.length() > 120) {
            throw new IllegalArgumentException("Process name is required and must have at most 120 characters");
        }
        if (processedWeight == null || processedWeight.value().signum() <= 0) {
            throw new IllegalArgumentException("Processed weight must be positive");
        }
        if (startedAt == null || status == null) {
            throw new IllegalArgumentException("Start date and status are required");
        }
        if (finishedAt != null && finishedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("Finish date must not precede the start date");
        }
        if (status == ProductionStatus.COMPLETED && finishedAt == null) {
            throw new IllegalArgumentException("Finish date is required for completed processes");
        }
    }
}
