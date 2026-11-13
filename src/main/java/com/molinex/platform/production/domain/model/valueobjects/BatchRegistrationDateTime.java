package com.molinex.platform.production.domain.model.valueobjects;

import java.time.Instant;
import java.util.Objects;

public record BatchRegistrationDateTime(Instant value) {

    public BatchRegistrationDateTime {
        Objects.requireNonNull(value, "Registration date is required");
    }
}
