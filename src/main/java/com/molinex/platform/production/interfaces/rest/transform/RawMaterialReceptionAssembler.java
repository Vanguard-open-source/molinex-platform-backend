package com.molinex.platform.production.interfaces.rest.transform;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.commands.RecordRawMaterialReceptionCommand;
import com.molinex.platform.production.interfaces.rest.resources.RawMaterialReceptionResource;
import com.molinex.platform.production.interfaces.rest.resources.RecordRawMaterialReceptionResource;

public final class RawMaterialReceptionAssembler {

    private RawMaterialReceptionAssembler() {
    }

    public static RecordRawMaterialReceptionCommand toCommand(RecordRawMaterialReceptionResource resource) {
        return new RecordRawMaterialReceptionCommand(resource.receivedAt(), resource.supplierName(),
                resource.originDescription(), resource.quantityValue(), resource.quantityUnit());
    }

    public static RawMaterialReceptionResource toResource(RawMaterialReception reception) {
        return new RawMaterialReceptionResource(reception.getId().value(), reception.getReceivedAt(),
                reception.getSupplier().name(), reception.getOrigin().description(),
                reception.getQuantity().value(), reception.getQuantity().unit());
    }
}
