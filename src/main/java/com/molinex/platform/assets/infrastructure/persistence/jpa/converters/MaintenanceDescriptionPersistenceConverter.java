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
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceDescription;

@Converter
public class MaintenanceDescriptionPersistenceConverter implements AttributeConverter<MaintenanceDescription, String> {
    public String convertToDatabaseColumn(MaintenanceDescription value) {
        return value == null ? null : value.value();
    }

    public MaintenanceDescription convertToEntityAttribute(String value) {
        return value == null ? null : new MaintenanceDescription(value);
    }
}
