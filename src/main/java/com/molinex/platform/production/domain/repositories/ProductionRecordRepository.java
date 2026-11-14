package com.molinex.platform.production.domain.repositories;

import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductionRecordRepository {

    ProductionRecord save(ProductionRecord record);

    Optional<ProductionRecord> findById(ProductionRecordId id);

    List<ProductionRecord> findProcesses(ProductionBatchId batchId);

    List<ProductionRecord> findHistory(Instant from, Instant to);
}
