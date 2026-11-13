package com.molinex.platform.quality.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;


public record IndicatorResource(
        String indicator,
        BigDecimal value,
        String unit,
        boolean available,
        String reason,
        String source,
        Integer sourceId,
        Integer productionRecordId,
        Instant observedAt
) {
}
