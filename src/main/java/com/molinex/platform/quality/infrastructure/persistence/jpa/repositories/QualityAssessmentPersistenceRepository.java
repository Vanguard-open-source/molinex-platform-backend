package com.molinex.platform.quality.infrastructure.persistence.jpa.repositories;

import com.molinex.platform.quality.infrastructure.persistence.jpa.entities.QualityAssessmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityAssessmentPersistenceRepository extends
        JpaRepository<QualityAssessmentPersistenceEntity, Integer> {
}
