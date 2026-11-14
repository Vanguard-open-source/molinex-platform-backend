package com.molinex.platform.production.infrastructure.persistence.jpa.adapters;

import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;

import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import com.molinex.platform.production.infrastructure.persistence.jpa.assemblers.ProductionBatchPersistenceAssembler;
import com.molinex.platform.production.infrastructure.persistence.jpa.repositories.ProductionBatchPersistenceRepository;
import com.molinex.platform.production.infrastructure.persistence.jpa.repositories.RawMaterialReceptionPersistenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductionBatchRepositoryImpl implements ProductionBatchRepository {

    private final ProductionBatchPersistenceRepository batches;
    private final RawMaterialReceptionPersistenceRepository receptions;

    public ProductionBatchRepositoryImpl(ProductionBatchPersistenceRepository batches,
                                         RawMaterialReceptionPersistenceRepository receptions) {
        this.batches = batches;
        this.receptions = receptions;
    }

    @Override
    public boolean existsByCode(BatchCode code) {
        return batches.existsByCode(code.value());
    }

    @Override
    public ProductionBatch save(ProductionBatch batch) {
        if (batch.id() != null) {
            throw new IllegalArgumentException("A production batch can only be registered once");
        }
        var reception = receptions.getReferenceById(batch.receptionId().value());
        var entity = ProductionBatchPersistenceAssembler.toEntity(batch, reception);
        return ProductionBatchPersistenceAssembler.toDomain(batches.saveAndFlush(entity));
    }

    @Override
    public List<ProductionBatch> findAvailable() {
        return batches.findAll(Sort.by("id")).stream().map(ProductionBatchPersistenceAssembler::toDomain).toList();
    }

    @Override
    public Optional<ProductionBatch> findById(ProductionBatchId id) {
        return batches.findById(id.value()).map(ProductionBatchPersistenceAssembler::toDomain);
    }
}
