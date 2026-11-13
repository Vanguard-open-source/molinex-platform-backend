package com.molinex.platform.quality.domain.model.valueobjects;

import java.math.BigDecimal;

public record Percentage(
        BigDecimal value
) {
    public Percentage {
        if (value == null || value.signum() < 0 || value.compareTo(new BigDecimal("100")) > 0 ||
                value.stripTrailingZeros().scale() > 2) throw new IllegalArgumentException("Percentage must be valid");
    }
}
