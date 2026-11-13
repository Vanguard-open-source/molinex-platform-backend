package com.molinex.platform.assets.infrastructure.persistence.jpa.converters;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.molinex.platform.assets.domain.model.valueobjects.AnomalyReference;

@Converter
public class AnomalyReferencePersistenceConverter implements AttributeConverter<AnomalyReference, Long> {
    public Long convertToDatabaseColumn(AnomalyReference value) {
        return value == null ? null : value.value();
    }

    public AnomalyReference convertToEntityAttribute(Long value) {
        return value == null ? null : new AnomalyReference(value);
    }
}
