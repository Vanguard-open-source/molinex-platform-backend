package com.molinex.platform.assets.infrastructure.persistence.jpa.adapters;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;
import com.molinex.platform.assets.infrastructure.persistence.jpa.repositories.MaintenanceRecordPersistenceRepository;
import com.molinex.platform.assets.infrastructure.persistence.jpa.assemblers.MaintenanceRecordPersistenceAssembler;
import com.molinex.platform.shared.infrastructure.events.DomainEventPublisher;

@Repository
@RequiredArgsConstructor
@Transactional
public class MaintenanceRecordRepositoryImpl implements MaintenanceRecordRepository {
    private final MaintenanceRecordPersistenceRepository persistence;
    private final DomainEventPublisher events;

    public MaintenanceRecord save(MaintenanceRecord aggregate) {
        boolean isNew = aggregate.getId() == null;
        var entity = persistence.saveAndFlush(MaintenanceRecordPersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new MaintenanceRecordId(entity.getId()));
        if (isNew) aggregate.onCreated();
        events.publishAndClearEvents(aggregate);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<MaintenanceRecord> findById(MaintenanceRecordId id) {
        return persistence.findById(id.value()).map(MaintenanceRecordPersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceRecord> findAll() {
        return persistence.findAll().stream().map(MaintenanceRecordPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
