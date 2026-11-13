package com.molinex.platform.production.domain.model.aggregates;

import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionDetails;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;

import java.time.Instant;

public class ProductionRecord {

    private final ProductionRecordId id;
    private final ProductionBatchId batchId;
    private final Instant recordedAt;
    private ProductionDetails details;

    public ProductionRecord(ProductionRecordId id, ProductionBatchId batchId, ProductionDetails details,
                            Instant recordedAt) {
        if (batchId == null || details == null || recordedAt == null) {
            throw new IllegalArgumentException("Batch ID, process details and recording date are required");
        }
        this.id = id;
        this.batchId = batchId;
        this.details = details;
        this.recordedAt = recordedAt;
    }

    public void updateDetails(ProductionBatchId batchId, ProductionDetails details) {
        if (!this.batchId.equals(batchId)) {
            throw new IllegalArgumentException("The original batch cannot change");
        }
        if (details == null) {
            throw new IllegalArgumentException("Process details are required");
        }
        this.details = details;
    }

    public ProductionRecordId id() {
        return id;
    }

    public ProductionBatchId batchId() {
        return batchId;
    }

    public Instant recordedAt() {
        return recordedAt;
    }

    public ProductionDetails details() {
        return details;
    }
}
