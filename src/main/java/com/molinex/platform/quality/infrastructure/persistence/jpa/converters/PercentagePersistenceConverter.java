package com.molinex.platform.quality.infrastructure.persistence.jpa.converters;

import com.molinex.platform.quality.domain.model.valueobjects.Percentage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter
public class PercentagePersistenceConverter implements AttributeConverter<Percentage, BigDecimal> {
    public BigDecimal convertToDatabaseColumn(Percentage value) {
        return value == null ? null : value.value();
    }

    public Percentage convertToEntityAttribute(BigDecimal value) {
        return value == null ? null : new Percentage(value);
    }
}
