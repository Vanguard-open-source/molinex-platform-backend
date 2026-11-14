package com.molinex.platform.quality.infrastructure.persistence.jpa.converters;

import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WasteRecordIdPersistenceConverter implements AttributeConverter<WasteRecordId, Integer> {
    public Integer convertToDatabaseColumn(WasteRecordId value) {
        return value == null ? null : value.value();
    }

    public WasteRecordId convertToEntityAttribute(Integer value) {
        return value == null ? null : new WasteRecordId(value);
    }
}
