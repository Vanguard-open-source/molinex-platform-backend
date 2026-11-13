package com.molinex.platform.production.domain.model.aggregates;

import com.molinex.platform.production.domain.model.events.ProductionBatchRegistered;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.BatchRegistrationDateTime;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;

import java.time.Instant;
import java.util.Objects;

public final class ProductionBatch {

    private ProductionBatchId id;
    private final BatchCode code;
    private final RawMaterialReceptionId receptionId;
    private final BatchRegistrationDateTime registeredAt;

    private ProductionBatch(BatchCode code, RawMaterialReceptionId receptionId,
                            BatchRegistrationDateTime registeredAt) {
        this.code = Objects.requireNonNull(code);
        this.receptionId = Objects.requireNonNull(receptionId);
        this.registeredAt = Objects.requireNonNull(registeredAt);
    }

    public static ProductionBatch register(BatchCode code, RawMaterialReceptionId receptionId,
                                           BatchRegistrationDateTime registeredAt) {
        return new ProductionBatch(code, receptionId, registeredAt);
    }

    public void assignId(ProductionBatchId id) {
        if (this.id != null) {
            throw new IllegalStateException("The batch already has an identity");
        }
        this.id = Objects.requireNonNull(id);
    }

    public ProductionBatchRegistered registrationEvent(Instant occurredAt) {
        if (id == null) {
            throw new IllegalStateException("A batch identity is required for the event");
        }
        return new ProductionBatchRegistered(id, receptionId, Objects.requireNonNull(occurredAt));
    }

    public ProductionBatchId id() {
        return id;
    }

    public BatchCode code() {
        return code;
    }

    public RawMaterialReceptionId receptionId() {
        return receptionId;
    }

    public BatchRegistrationDateTime registeredAt() {
        return registeredAt;
    }
}
