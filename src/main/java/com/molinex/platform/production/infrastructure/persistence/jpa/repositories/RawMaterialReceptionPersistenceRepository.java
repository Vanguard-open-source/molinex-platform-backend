package com.molinex.platform.production.infrastructure.persistence.jpa.repositories;

import com.molinex.platform.production.infrastructure.persistence.jpa.entities.RawMaterialReceptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialReceptionPersistenceRepository
        extends JpaRepository<RawMaterialReceptionPersistenceEntity, Long> {
}
