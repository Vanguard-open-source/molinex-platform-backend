package com.molinex.platform.quality.domain.model.queries;

import java.time.Instant;


public record GetYieldIndicatorsQuery(
        Instant from,
        Instant to,
        Long productionRecordId
) {
}
