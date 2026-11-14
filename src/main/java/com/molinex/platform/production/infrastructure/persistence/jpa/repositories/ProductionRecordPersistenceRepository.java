package com.molinex.platform.production.infrastructure.persistence.jpa.repositories;

import com.molinex.platform.production.infrastructure.persistence.jpa.entities.ProductionRecordPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ProductionRecordPersistenceRepository
        extends JpaRepository<ProductionRecordPersistenceEntity, Long> {

    List<ProductionRecordPersistenceEntity> findByBatch_IdOrderByIdAsc(Long batchId);

    List<ProductionRecordPersistenceEntity> findByRecordedAtBetweenOrderByRecordedAtAscIdAsc(Instant from, Instant to);

    List<ProductionRecordPersistenceEntity> findByRecordedAtGreaterThanEqualOrderByRecordedAtAscIdAsc(Instant from);

    List<ProductionRecordPersistenceEntity> findByRecordedAtLessThanEqualOrderByRecordedAtAscIdAsc(Instant to);
}
