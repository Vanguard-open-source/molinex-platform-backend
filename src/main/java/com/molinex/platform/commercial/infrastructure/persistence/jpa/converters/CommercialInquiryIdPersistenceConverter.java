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
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;

@Converter
public class CommercialInquiryIdPersistenceConverter implements AttributeConverter<CommercialInquiryId, Long> {
    public Long convertToDatabaseColumn(CommercialInquiryId value) {
        return value == null ? null : value.value();
    }

    public CommercialInquiryId convertToEntityAttribute(Long value) {
        return value == null ? null : new CommercialInquiryId(value);
    }
}
