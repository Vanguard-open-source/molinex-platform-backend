package com.molinex.platform.quality.infrastructure.persistence.jpa.converters;

import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductionRecordIdPersistenceConverter implements AttributeConverter<ProductionRecordId, Integer> {
    public Integer convertToDatabaseColumn(ProductionRecordId value) {
        return value == null ? null : value.value();
    }

    public ProductionRecordId convertToEntityAttribute(Integer value) {
        return value == null ? null : new ProductionRecordId(value);
    }
}
