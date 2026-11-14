package com.molinex.platform.production.infrastructure.persistence.jpa.adapters;

import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.production.domain.repositories.ProductionRecordRepository;
import com.molinex.platform.production.infrastructure.persistence.jpa.assemblers.ProductionRecordPersistenceAssembler;
import com.molinex.platform.production.infrastructure.persistence.jpa.entities.ProductionRecordPersistenceEntity;
import com.molinex.platform.production.infrastructure.persistence.jpa.repositories.ProductionBatchPersistenceRepository;
import com.molinex.platform.production.infrastructure.persistence.jpa.repositories.ProductionRecordPersistenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductionRecordRepositoryImpl implements ProductionRecordRepository {

    private final ProductionRecordPersistenceRepository records;
    private final ProductionBatchPersistenceRepository batches;

    public ProductionRecordRepositoryImpl(ProductionRecordPersistenceRepository records,
                                          ProductionBatchPersistenceRepository batches) {
        this.records = records;
        this.batches = batches;
    }

    @Override
    @Transactional
    public ProductionRecord save(ProductionRecord record) {
        var entity = record.id() == null
                ? new ProductionRecordPersistenceEntity(batches.getReferenceById(record.batchId().value()), record.recordedAt())
                : records.findById(record.id().value()).orElseThrow();
        ProductionRecordPersistenceAssembler.setDetails(entity, record.details());
        return ProductionRecordPersistenceAssembler.toDomain(records.saveAndFlush(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductionRecord> findById(ProductionRecordId id) {
        return records.findById(id.value()).map(ProductionRecordPersistenceAssembler::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionRecord> findProcesses(ProductionBatchId batchId) {
        var result = batchId == null ? records.findAll(Sort.by("id"))
                : records.findByBatch_IdOrderByIdAsc(batchId.value());
        return result.stream().map(ProductionRecordPersistenceAssembler::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionRecord> findHistory(Instant from, Instant to) {
        List<ProductionRecordPersistenceEntity> result;
        if (from == null && to == null) {
            result = records.findAll(Sort.by("recordedAt", "id"));
        } else if (from == null) {
            result = records.findByRecordedAtLessThanEqualOrderByRecordedAtAscIdAsc(to);
        } else if (to == null) {
            result = records.findByRecordedAtGreaterThanEqualOrderByRecordedAtAscIdAsc(from);
        } else {
            result = records.findByRecordedAtBetweenOrderByRecordedAtAscIdAsc(from, to);
        }
        return result.stream().map(ProductionRecordPersistenceAssembler::toDomain).toList();
    }
}
