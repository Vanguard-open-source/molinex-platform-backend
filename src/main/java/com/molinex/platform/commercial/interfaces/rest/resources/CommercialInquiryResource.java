package com.molinex.platform.commercial.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;





public record CommercialInquiryResource(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        String companyName,
        String message,
        Instant submittedAt
) {
}
