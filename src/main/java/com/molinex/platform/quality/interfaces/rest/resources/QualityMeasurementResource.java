package com.molinex.platform.quality.interfaces.rest.resources;

import com.molinex.platform.quality.domain.model.valueobjects.QualityIndicator;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QualityMeasurementResource(
        @NotNull
        QualityIndicator indicator,
        @NotNull
        BigDecimal value,
        @NotNull
        BigDecimal expectedMinimum,
        @NotNull
        BigDecimal expectedMaximum
) {
}
