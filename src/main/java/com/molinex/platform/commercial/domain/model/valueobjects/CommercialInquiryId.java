package com.molinex.platform.commercial.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record CommercialInquiryId(
        Long value
) {
    public CommercialInquiryId {
        if (value == null || value <= 0) throw new IllegalArgumentException("CommercialInquiryId must be valid");
    }
}
