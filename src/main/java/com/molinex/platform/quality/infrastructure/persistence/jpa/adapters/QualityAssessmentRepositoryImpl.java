package com.molinex.platform.quality.infrastructure.persistence.jpa.adapters;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;
import com.molinex.platform.quality.domain.repositories.QualityAssessmentRepository;
import com.molinex.platform.quality.infrastructure.persistence.jpa.assemblers.QualityAssessmentPersistenceAssembler;
import com.molinex.platform.quality.infrastructure.persistence.jpa.repositories.QualityAssessmentPersistenceRepository;
import com.molinex.platform.shared.infrastructure.events.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class QualityAssessmentRepositoryImpl implements QualityAssessmentRepository {
    private final QualityAssessmentPersistenceRepository persistence;
    private final DomainEventPublisher events;

    public QualityAssessment save(QualityAssessment aggregate) {
        boolean isNew = aggregate.getId() == null;
        var entity = persistence.saveAndFlush(QualityAssessmentPersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new QualityAssessmentId(entity.getId()));
        if (isNew) aggregate.onCreated();
        events.publishAndClearEvents(aggregate);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<QualityAssessment> findById(QualityAssessmentId id) {
        return persistence.findById(id.value()).map(QualityAssessmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<QualityAssessment> findAll() {
        return persistence.findAll().stream().map(QualityAssessmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
