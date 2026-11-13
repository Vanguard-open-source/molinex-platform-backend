package com.molinex.platform.quality.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record RecordQualityResultsResource(
        @NotNull
        Integer productionRecordId,
        @NotNull
        @Valid
        List<
                @NotNull
                @Valid
                        QualityMeasurementResource> measurements,
        @NotNull
        Instant assessedAt
) {
}
