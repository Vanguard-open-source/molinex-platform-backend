package com.molinex.platform.production.interfaces.rest.resources;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordRawMaterialReceptionResource(
        Instant receivedAt,
        String supplierName,
        String originDescription,
        BigDecimal quantityValue,
        MeasurementUnit quantityUnit
) {
}
