package com.molinex.platform.commercial.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record InquiryMessage(
        String value
) {
    public InquiryMessage {
        if (value != null && value.length() > 2000)
            throw new IllegalArgumentException("value exceeds the storage limit of 2000 characters");
        if (value == null || value.isBlank()) throw new IllegalArgumentException("InquiryMessage must be valid");
    }
}
