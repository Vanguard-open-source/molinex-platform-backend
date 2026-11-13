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
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;

@Converter
public class MachineModelPersistenceConverter implements AttributeConverter<MachineModel, String> {
    public String convertToDatabaseColumn(MachineModel value) {
        return value == null ? null : value.value();
    }

    public MachineModel convertToEntityAttribute(String value) {
        return value == null ? null : new MachineModel(value);
    }
}
