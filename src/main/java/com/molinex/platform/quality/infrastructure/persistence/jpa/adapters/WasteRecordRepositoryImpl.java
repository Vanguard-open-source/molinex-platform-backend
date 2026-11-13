package com.molinex.platform.quality.infrastructure.persistence.jpa.adapters;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;
import com.molinex.platform.quality.domain.repositories.WasteRecordRepository;
import com.molinex.platform.quality.infrastructure.persistence.jpa.assemblers.WasteRecordPersistenceAssembler;
import com.molinex.platform.quality.infrastructure.persistence.jpa.repositories.WasteRecordPersistenceRepository;
import com.molinex.platform.shared.infrastructure.events.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class WasteRecordRepositoryImpl implements WasteRecordRepository {
    private final WasteRecordPersistenceRepository persistence;
    private final DomainEventPublisher events;

    public WasteRecord save(WasteRecord aggregate) {
        boolean isNew = aggregate.getId() == null;
        var entity = persistence.saveAndFlush(WasteRecordPersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new WasteRecordId(entity.getId()));
        if (isNew) aggregate.onCreated();
        events.publishAndClearEvents(aggregate);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<WasteRecord> findById(WasteRecordId id) {
        return persistence.findById(id.value()).map(WasteRecordPersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<WasteRecord> findAll() {
        return persistence.findAll().stream().map(WasteRecordPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
