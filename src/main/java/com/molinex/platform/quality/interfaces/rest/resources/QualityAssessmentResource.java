package com.molinex.platform.quality.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;


public record QualityAssessmentResource(
        Integer id,
        Long productionRecordId,
        List<QualityMeasurementResource> measurements,
        Instant assessedAt
) {
}
