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
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.infrastructure.persistence.jpa.repositories.MachinePersistenceRepository;
import com.molinex.platform.assets.infrastructure.persistence.jpa.assemblers.MachinePersistenceAssembler;
import com.molinex.platform.shared.infrastructure.events.DomainEventPublisher;

@Repository
@RequiredArgsConstructor
@Transactional
public class MachineRepositoryImpl implements MachineRepository {
    private final MachinePersistenceRepository persistence;
    private final DomainEventPublisher events;

    public Machine save(Machine aggregate) {
        boolean isNew = aggregate.getId() == null;
        var entity = persistence.saveAndFlush(MachinePersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new MachineId(entity.getId()));
        if (isNew) aggregate.onCreated();
        events.publishAndClearEvents(aggregate);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<Machine> findById(MachineId id) {
        return persistence.findById(id.value()).map(MachinePersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<Machine> findAll() {
        return persistence.findAll().stream().map(MachinePersistenceAssembler::toDomainFromPersistence).toList();
    }

    public boolean existsByCode(MachineCode code) {
        return persistence.existsByCode(code);
    }
}
