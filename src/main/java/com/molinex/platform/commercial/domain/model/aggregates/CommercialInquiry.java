package com.molinex.platform.commercial.domain.model.aggregates;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;
import com.molinex.platform.commercial.domain.model.valueobjects.ContactInformation;
import com.molinex.platform.commercial.domain.model.valueobjects.InquiryMessage;

import com.molinex.platform.commercial.domain.model.events.CommercialInquirySubmitted;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

@Getter
public class CommercialInquiry extends AbstractDomainAggregateRoot<CommercialInquiry> {
    private CommercialInquiryId id;
    private ContactInformation contact;
    private InquiryMessage message;
    private Instant submittedAt;

    public CommercialInquiry(CommercialInquiryId id, ContactInformation contact, InquiryMessage message,
            Instant submittedAt) {
        this.id = id;
        if (contact == null) throw new IllegalArgumentException("contact is required");
        if (message == null) throw new IllegalArgumentException("message is required");
        if (submittedAt == null) throw new IllegalArgumentException("submittedAt is required");

        this.contact = contact;
        this.message = message;
        this.submittedAt = submittedAt;
    }

    public void assignId(CommercialInquiryId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onCreated() {
        registerDomainEvent(new CommercialInquirySubmitted(id.value()));
    }

}
