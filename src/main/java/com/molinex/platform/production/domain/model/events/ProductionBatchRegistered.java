package com.molinex.platform.production.domain.model.events;

import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;

import java.time.Instant;

public record ProductionBatchRegistered(
        ProductionBatchId batchId, RawMaterialReceptionId receptionId, Instant occurredAt
) {
}
