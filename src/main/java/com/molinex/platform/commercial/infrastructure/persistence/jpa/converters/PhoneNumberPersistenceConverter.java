package com.molinex.platform.commercial.infrastructure.persistence.jpa.converters;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.molinex.platform.commercial.domain.model.valueobjects.PhoneNumber;

@Converter
public class PhoneNumberPersistenceConverter implements AttributeConverter<PhoneNumber, String> {
    public String convertToDatabaseColumn(PhoneNumber value) {
        return value == null ? null : value.value();
    }

    public PhoneNumber convertToEntityAttribute(String value) {
        return value == null ? null : new PhoneNumber(value);
    }
}
