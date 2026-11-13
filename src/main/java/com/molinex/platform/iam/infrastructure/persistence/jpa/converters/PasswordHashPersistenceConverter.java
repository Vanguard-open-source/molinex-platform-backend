package com.molinex.platform.iam.infrastructure.persistence.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.molinex.platform.iam.domain.model.valueobjects.PasswordHash;

@Converter
public class PasswordHashPersistenceConverter implements AttributeConverter<PasswordHash, String> {
    public String convertToDatabaseColumn(PasswordHash value) {
        return value == null ? null : value.value();
    }

    public PasswordHash convertToEntityAttribute(String value) {
        return value == null ? null : new PasswordHash(value);
    }
}
