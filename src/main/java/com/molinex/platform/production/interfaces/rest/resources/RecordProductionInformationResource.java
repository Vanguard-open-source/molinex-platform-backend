package com.molinex.platform.production.interfaces.rest.resources;

import com.molinex.platform.production.domain.model.valueobjects.ProductionStatus;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;

public record RecordProductionInformationResource(
        Long batchId, String processName, BigDecimal processedWeightValue, MeasurementUnit processedWeightUnit,
        Instant startedAt, Instant finishedAt, ProductionStatus status, Instant recordedAt
) {
}
