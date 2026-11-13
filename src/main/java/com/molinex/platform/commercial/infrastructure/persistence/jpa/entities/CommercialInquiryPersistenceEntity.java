package com.molinex.platform.commercial.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.InquiryMessage;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.converters.InquiryMessagePersistenceConverter;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.embeddables
        .ContactInformationPersistenceEmbeddable;
import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "commercial_inquiries")
@Getter
@Setter
@NoArgsConstructor
public class CommercialInquiryPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ContactInformationPersistenceEmbeddable contact;
    @Convert(converter = InquiryMessagePersistenceConverter.class)
    @Column(nullable = false, length = 2000)
    private InquiryMessage message;
    @Column(nullable = false)
    private Instant submittedAt;
}
