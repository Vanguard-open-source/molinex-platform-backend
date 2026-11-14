package com.molinex.platform.commercial.infrastructure.persistence.jpa.adapters;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.domain.repositories.CommercialInquiryRepository;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.repositories
        .CommercialInquiryPersistenceRepository;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.assemblers.CommercialInquiryPersistenceAssembler;
import com.molinex.platform.shared.infrastructure.events.DomainEventPublisher;

@Repository
@RequiredArgsConstructor
@Transactional
public class CommercialInquiryRepositoryImpl implements CommercialInquiryRepository {
    private final CommercialInquiryPersistenceRepository persistence;
    private final DomainEventPublisher events;

    public CommercialInquiry save(CommercialInquiry aggregate) {
        boolean isNew = aggregate.getId() == null;
        var entity = persistence.saveAndFlush(CommercialInquiryPersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new CommercialInquiryId(entity.getId()));
        if (isNew) aggregate.onCreated();
        events.publishAndClearEvents(aggregate);
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<CommercialInquiry> findById(CommercialInquiryId id) {
        return persistence.findById(id.value()).map(CommercialInquiryPersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<CommercialInquiry> findAll() {
        return persistence.findAll().stream().map(CommercialInquiryPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
