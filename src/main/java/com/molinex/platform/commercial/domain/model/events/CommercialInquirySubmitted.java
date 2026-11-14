package com.molinex.platform.commercial.domain.model.events;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record CommercialInquirySubmitted(
        Long id
) {
}
