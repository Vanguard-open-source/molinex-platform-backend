package com.molinex.platform.commercial.infrastructure.persistence.jpa.assemblers;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;

import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.entities.CommercialInquiryPersistenceEntity;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.embeddables
        .ContactInformationPersistenceEmbeddable;

public final class CommercialInquiryPersistenceAssembler {
    private CommercialInquiryPersistenceAssembler() {
    }

    public static CommercialInquiry toDomainFromPersistence(CommercialInquiryPersistenceEntity entity) {
        return new CommercialInquiry(entity.getId() == null ? null : new CommercialInquiryId(entity.getId()),
                entity.getContact() == null ? null : entity.getContact().toDomain(), entity.getMessage(),
                entity.getSubmittedAt());
    }

    public static CommercialInquiryPersistenceEntity toPersistenceFromDomain(CommercialInquiry aggregate) {
        var entity = new CommercialInquiryPersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setContact(aggregate.getContact() == null ? null :
                ContactInformationPersistenceEmbeddable.fromDomain(aggregate.getContact()));
        entity.setMessage(aggregate.getMessage());
        entity.setSubmittedAt(aggregate.getSubmittedAt());
        return entity;
    }
}
