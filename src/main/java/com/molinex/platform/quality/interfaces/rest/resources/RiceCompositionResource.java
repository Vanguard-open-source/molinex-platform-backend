package com.molinex.platform.quality.interfaces.rest.resources;

import java.util.List;


public record RiceCompositionResource(
        Long batchId,
        boolean available,
        String reason,
        List<QualityAssessmentResource> assessments
) {
}
