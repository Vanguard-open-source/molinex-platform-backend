package com.molinex.platform.iam.infrastructure.persistence.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.molinex.platform.iam.domain.model.valueobjects.UserProfile;

@Converter
public class UserProfilePersistenceConverter implements AttributeConverter<UserProfile, String> {
    public String convertToDatabaseColumn(UserProfile value) {
        return value == null ? null : value.fullName();
    }

    public UserProfile convertToEntityAttribute(String value) {
        return value == null ? null : new UserProfile(value);
    }
}
