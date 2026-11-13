package com.molinex.platform.production.interfaces.rest.transform;

import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.commands.RecordProductionInformationCommand;
import com.molinex.platform.production.domain.model.commands.UpdateProductionInformationCommand;
import com.molinex.platform.production.interfaces.rest.resources.ProductionRecordResource;
import com.molinex.platform.production.interfaces.rest.resources.RecordProductionInformationResource;
import com.molinex.platform.production.interfaces.rest.resources.UpdateProductionInformationResource;

public final class ProductionRecordAssembler {

    private ProductionRecordAssembler() {
    }

    public static RecordProductionInformationCommand toCommand(RecordProductionInformationResource resource) {
        return new RecordProductionInformationCommand(resource.batchId(), resource.processName(),
                resource.processedWeightValue(), resource.processedWeightUnit(), resource.startedAt(),
                resource.finishedAt(), resource.status(), resource.recordedAt());
    }

    public static UpdateProductionInformationCommand toCommand(long id, UpdateProductionInformationResource resource) {
        return new UpdateProductionInformationCommand(id, resource.batchId(), resource.processName(),
                resource.processedWeightValue(), resource.processedWeightUnit(), resource.startedAt(),
                resource.finishedAt(), resource.status());
    }

    public static ProductionRecordResource toResource(ProductionRecord record) {
        var details = record.details();
        return new ProductionRecordResource(record.id().value(), record.batchId().value(), details.processName(),
                details.processedWeight().value(), details.processedWeight().unit(), details.startedAt(),
                details.finishedAt(), details.status(), record.recordedAt());
    }
}
