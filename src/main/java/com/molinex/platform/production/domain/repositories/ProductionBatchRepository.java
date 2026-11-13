package com.molinex.platform.production.domain.repositories;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;

import java.util.List;
import java.util.Optional;

public interface ProductionBatchRepository {
    ProductionBatch save(ProductionBatch batch);
    Optional<ProductionBatch> findById(ProductionBatchId id);
    List<ProductionBatch> findAvailable();
    boolean existsByCode(BatchCode code);
}
