package com.molinex.platform.assets.domain.model.valueobjects;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public record TechnicianReference(
        Long principalId,
        String displayName
) {
    public TechnicianReference {
        if (displayName != null && displayName.length() > 150)
            throw new IllegalArgumentException("displayName exceeds the storage limit of 150 characters");
        if (principalId == null || principalId <= 0 || displayName == null || displayName.isBlank())
            throw new IllegalArgumentException("Responsible principal identifier and display name are required");
    }
}
