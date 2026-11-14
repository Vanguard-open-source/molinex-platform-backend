package com.molinex.platform.commercial.domain.model.commands;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;





public record SubmitCommercialInquiryCommand(
        String fullName,
        String email,
        String phoneNumber,
        String companyName,
        String message
) {
}
