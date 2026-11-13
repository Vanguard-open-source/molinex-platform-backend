package com.molinex.platform.quality.infrastructure.persistence.jpa.converters;

import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QualityAssessmentIdPersistenceConverter implements AttributeConverter<QualityAssessmentId, Integer> {
    public Integer convertToDatabaseColumn(QualityAssessmentId value) {
        return value == null ? null : value.value();
    }

    public QualityAssessmentId convertToEntityAttribute(Integer value) {
        return value == null ? null : new QualityAssessmentId(value);
    }
}
