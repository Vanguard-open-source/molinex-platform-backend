package com.molinex.platform.quality.infrastructure.persistence.jpa.converters;

import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProductionRecordIdPersistenceConverter implements AttributeConverter<ProductionRecordId, Long> {
    public Long convertToDatabaseColumn(ProductionRecordId value) {
        return value == null ? null : value.value();
    }

    public ProductionRecordId convertToEntityAttribute(Long value) {
        return value == null ? null : new ProductionRecordId(value);
    }
}
