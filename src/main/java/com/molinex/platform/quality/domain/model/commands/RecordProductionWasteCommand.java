package com.molinex.platform.quality.domain.model.commands;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;


public record RecordProductionWasteCommand(
        Integer productionRecordId,
        BigDecimal quantityValue,
        MeasurementUnit quantityUnit,
        BigDecimal baseWeightValue,
        MeasurementUnit baseWeightUnit,
        Instant recordedAt
) {
}
