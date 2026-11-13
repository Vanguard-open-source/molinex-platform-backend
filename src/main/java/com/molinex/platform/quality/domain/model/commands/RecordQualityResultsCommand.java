package com.molinex.platform.quality.domain.model.commands;

import com.molinex.platform.quality.domain.model.valueobjects.QualityMeasurement;

import java.time.Instant;
import java.util.List;


public record RecordQualityResultsCommand(
        Integer productionRecordId,
        List<QualityMeasurement> measurements,
        Instant assessedAt
) {
}
