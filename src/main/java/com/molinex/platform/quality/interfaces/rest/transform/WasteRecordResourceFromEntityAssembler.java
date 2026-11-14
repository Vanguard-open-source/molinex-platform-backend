package com.molinex.platform.quality.interfaces.rest.transform;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.interfaces.rest.resources.WasteRecordResource;

public final class WasteRecordResourceFromEntityAssembler {
    private WasteRecordResourceFromEntityAssembler() {
    }

    public static WasteRecordResource toResourceFromEntity(WasteRecord entity) {
        return new WasteRecordResource(entity.getId().value(), entity.getProductionRecordId().value(),
                entity.getQuantity().value(), entity.getQuantity().unit(),
                entity.getBaseWeight() == null ? null : entity.getBaseWeight().value(),
                entity.getBaseWeight() == null ? null : entity.getBaseWeight().unit(),
                entity.getPercentage() == null ? null : entity.getPercentage().value(), entity.getRecordedAt());
    }
}
