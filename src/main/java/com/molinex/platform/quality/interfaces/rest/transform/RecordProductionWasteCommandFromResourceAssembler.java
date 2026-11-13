package com.molinex.platform.quality.interfaces.rest.transform;

import com.molinex.platform.quality.domain.model.commands.RecordProductionWasteCommand;
import com.molinex.platform.quality.interfaces.rest.resources.RecordProductionWasteResource;

public final class RecordProductionWasteCommandFromResourceAssembler {
    private RecordProductionWasteCommandFromResourceAssembler() {
    }

    public static RecordProductionWasteCommand toCommandFromResource(RecordProductionWasteResource resource) {
        return new RecordProductionWasteCommand(resource.productionRecordId(), resource.quantityValue(),
                resource.quantityUnit(), resource.baseWeightValue(), resource.baseWeightUnit(), resource.recordedAt());
    }
}
