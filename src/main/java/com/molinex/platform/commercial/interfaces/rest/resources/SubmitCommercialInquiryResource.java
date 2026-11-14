package com.molinex.platform.commercial.interfaces.rest.resources;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SubmitCommercialInquiryResource(
        @NotBlank
        String fullName,
        @NotBlank
        @Email
        String email,
        String phoneNumber,
        @NotBlank
        String companyName,
        @NotBlank
        String message
) {
}
