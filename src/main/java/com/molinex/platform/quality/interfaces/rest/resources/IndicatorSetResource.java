package com.molinex.platform.quality.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;


public record IndicatorSetResource(
        Instant from,
        Instant to,
        Integer productionRecordId,
        List<IndicatorResource> indicators
) {
}
