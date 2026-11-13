package com.molinex.platform.iam.domain.model.valueobjects;

import java.util.Arrays;

public enum Role {
    ADMINISTRATOR("administrator"),
    TECHNICIAN("technician"),
    OPERATOR("operator");
    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Role fromValue(String value) {
        return Arrays.stream(values()).filter(r -> r.value.equals(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role must be administrator, technician or operator"));
    }
}
