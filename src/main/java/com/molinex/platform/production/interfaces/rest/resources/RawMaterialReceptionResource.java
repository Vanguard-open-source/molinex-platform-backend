package com.molinex.platform.production.interfaces.rest.resources;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;

public record RawMaterialReceptionResource(
        Long id,
        Instant receivedAt,
        String supplierName,
        String originDescription,
        BigDecimal quantityValue,
        MeasurementUnit quantityUnit
) {
}
