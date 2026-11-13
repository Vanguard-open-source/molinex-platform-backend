package com.molinex.platform.quality.domain.model.valueobjects;

import java.time.Instant;

public record IndicatorPeriod(
        Instant from,
        Instant to
) {
    public IndicatorPeriod {
        if (from != null && to != null && from.isAfter(to))
            throw new IllegalArgumentException("from must not follow to");
    }

    public boolean contains(Instant time) {
        return (from == null || !time.isBefore(from)) && (to == null || !time.isAfter(to));
    }
}
