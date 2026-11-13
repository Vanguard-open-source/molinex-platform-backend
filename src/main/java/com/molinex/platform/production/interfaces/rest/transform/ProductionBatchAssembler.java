package com.molinex.platform.production.interfaces.rest.transform;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.commands.RegisterProductionBatchCommand;
import com.molinex.platform.production.interfaces.rest.resources.ProductionBatchResource;
import com.molinex.platform.production.interfaces.rest.resources.RegisterProductionBatchResource;

public final class ProductionBatchAssembler {

    private ProductionBatchAssembler() {
    }

    public static RegisterProductionBatchCommand toCommand(RegisterProductionBatchResource resource) {
        return new RegisterProductionBatchCommand(resource.code(), resource.receptionId(), resource.registeredAt());
    }

    public static ProductionBatchResource toResource(ProductionBatch batch) {
        return new ProductionBatchResource(batch.id().value(), batch.code().value(),
                batch.receptionId().value(), batch.registeredAt().value());
    }
}
