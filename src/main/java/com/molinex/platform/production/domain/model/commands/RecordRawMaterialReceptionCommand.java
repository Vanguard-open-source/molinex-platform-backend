package com.molinex.platform.production.domain.model.commands;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordRawMaterialReceptionCommand(
        Instant receivedAt,
        String supplierName,
        String originDescription,
        BigDecimal quantityValue,
        MeasurementUnit quantityUnit
) {
}
