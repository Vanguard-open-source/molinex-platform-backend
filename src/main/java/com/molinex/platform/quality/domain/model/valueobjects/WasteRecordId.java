package com.molinex.platform.quality.domain.model.valueobjects;

public record WasteRecordId(
        Integer value
) {
    public WasteRecordId {
        if (value == null || value <= 0) throw new IllegalArgumentException("WasteRecordId must be valid");
    }
}
