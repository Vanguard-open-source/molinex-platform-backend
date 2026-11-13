package com.molinex.platform.iam.infrastructure.persistence.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;

@Converter
public class EmailAddressPersistenceConverter implements AttributeConverter<EmailAddress, String> {
    public String convertToDatabaseColumn(EmailAddress value) {
        return value == null ? null : value.value();
    }

    public EmailAddress convertToEntityAttribute(String value) {
        return value == null ? null : new EmailAddress(value);
    }
}
