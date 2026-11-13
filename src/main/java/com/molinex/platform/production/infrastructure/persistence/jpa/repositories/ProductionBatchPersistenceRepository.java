package com.molinex.platform.production.infrastructure.persistence.jpa.repositories;

import com.molinex.platform.production.infrastructure.persistence.jpa.entities.ProductionBatchPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionBatchPersistenceRepository extends JpaRepository<ProductionBatchPersistenceEntity, Long> {

    boolean existsByCode(String code);
}
