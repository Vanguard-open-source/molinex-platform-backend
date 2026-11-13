package com.molinex.platform.quality.interfaces.rest.resources;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordProductionWasteResource(
        @NotNull
        Integer productionRecordId,
        @NotNull
        BigDecimal quantityValue,
        @NotNull
        MeasurementUnit quantityUnit,
        BigDecimal baseWeightValue,
        MeasurementUnit baseWeightUnit,
        @NotNull
        Instant recordedAt
) {
}
