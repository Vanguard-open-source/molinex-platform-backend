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
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

@Converter
public class MaintenanceRecordIdPersistenceConverter implements AttributeConverter<MaintenanceRecordId, Long> {
    public Long convertToDatabaseColumn(MaintenanceRecordId value) {
        return value == null ? null : value.value();
    }

    public MaintenanceRecordId convertToEntityAttribute(Long value) {
        return value == null ? null : new MaintenanceRecordId(value);
    }
}
