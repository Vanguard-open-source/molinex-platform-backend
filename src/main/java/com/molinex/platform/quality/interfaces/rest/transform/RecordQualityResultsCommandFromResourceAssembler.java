package com.molinex.platform.quality.interfaces.rest.transform;

import com.molinex.platform.quality.domain.model.commands.RecordQualityResultsCommand;
import com.molinex.platform.quality.domain.model.valueobjects.Percentage;
import com.molinex.platform.quality.domain.model.valueobjects.QualityMeasurement;
import com.molinex.platform.quality.domain.model.valueobjects.QualityRange;
import com.molinex.platform.quality.interfaces.rest.resources.RecordQualityResultsResource;

public final class RecordQualityResultsCommandFromResourceAssembler {
    private RecordQualityResultsCommandFromResourceAssembler() {
    }

    public static RecordQualityResultsCommand toCommandFromResource(RecordQualityResultsResource resource) {
        return new RecordQualityResultsCommand(resource.productionRecordId(), resource.measurements().stream()
                .map(m -> new QualityMeasurement(m.indicator(), new Percentage(m.value()),
                        new QualityRange(new Percentage(m.expectedMinimum()), new Percentage(m.expectedMaximum()))))
                .toList(), resource.assessedAt());
    }
}
