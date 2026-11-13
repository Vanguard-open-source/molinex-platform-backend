package com.molinex.platform.quality.interfaces.rest.resources;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;


public record WasteRecordResource(
        Integer id,
        Integer productionRecordId,
        BigDecimal quantityValue,
        MeasurementUnit quantityUnit,
        BigDecimal baseWeightValue,
        MeasurementUnit baseWeightUnit,
        BigDecimal percentage,
        Instant recordedAt
) {
}
