package com.molinex.platform.commercial.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.interfaces.rest.resources.CommercialInquiryResource;

public final class CommercialInquiryResourceFromEntityAssembler {
    private CommercialInquiryResourceFromEntityAssembler() {
    }

    public static CommercialInquiryResource toResourceFromEntity(CommercialInquiry entity) {
        return new CommercialInquiryResource(entity.getId().value(), entity.getContact().fullName(),
                entity.getContact().email().value(),
                entity.getContact().phoneNumber() == null ? null : entity.getContact().phoneNumber().value(),
                entity.getContact().companyName(), entity.getMessage().value(), entity.getSubmittedAt());
    }
}
