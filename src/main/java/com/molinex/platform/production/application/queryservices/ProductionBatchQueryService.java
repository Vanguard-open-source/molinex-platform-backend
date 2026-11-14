package com.molinex.platform.production.application.queryservices;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionBatchQueryService {

    private final ProductionBatchRepository batches;

    public ProductionBatchQueryService(ProductionBatchRepository batches) {
        this.batches = batches;
    }

    public List<ProductionBatch> findAll() {
        return batches.findAvailable();
    }

    public Optional<ProductionBatch> findById(long id) {
        return batches.findById(new ProductionBatchId(id));
    }
}
