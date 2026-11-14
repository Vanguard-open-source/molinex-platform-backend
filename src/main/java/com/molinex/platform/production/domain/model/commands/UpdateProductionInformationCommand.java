package com.molinex.platform.production.domain.model.commands;

import com.molinex.platform.production.domain.model.valueobjects.ProductionStatus;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;

import java.math.BigDecimal;
import java.time.Instant;

public record UpdateProductionInformationCommand(
        Long id, Long batchId, String processName, BigDecimal processedWeightValue, MeasurementUnit processedWeightUnit,
        Instant startedAt, Instant finishedAt, ProductionStatus status
) {
}
