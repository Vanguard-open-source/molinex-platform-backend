package com.molinex.platform.quality.domain.model.valueobjects;

import java.math.BigDecimal;
import java.time.Instant;

public record IndicatorValue(
        String indicator,
        BigDecimal value,
        String unit,
        boolean available,
        String reason,
        String source,
        Integer sourceId,
        Long productionRecordId,
        Instant observedAt
) {
}
