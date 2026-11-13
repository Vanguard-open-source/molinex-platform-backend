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
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;

@Converter
public class MachineCodePersistenceConverter implements AttributeConverter<MachineCode, String> {
    public String convertToDatabaseColumn(MachineCode value) {
        return value == null ? null : value.value();
    }

    public MachineCode convertToEntityAttribute(String value) {
        return value == null ? null : new MachineCode(value);
    }
}
