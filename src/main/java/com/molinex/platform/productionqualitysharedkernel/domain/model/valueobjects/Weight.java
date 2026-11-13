package com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects;

import java.math.BigDecimal;

public record Weight(BigDecimal value, MeasurementUnit unit) {

    public Weight {
        if (value == null || unit == null) {
            throw new IllegalArgumentException("Weight value and unit are required");
        }
        var normalized = value.stripTrailingZeros();
        if (normalized.scale() > 4 || (long) normalized.precision() - normalized.scale() > 14) {
            throw new IllegalArgumentException("Weight exceeds decimal(18,4) precision");
        }
    }
}
