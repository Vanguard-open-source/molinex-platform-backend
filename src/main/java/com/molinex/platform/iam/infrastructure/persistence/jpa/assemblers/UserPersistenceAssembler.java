package com.molinex.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.iam.domain.model.valueobjects.UserId;

import com.molinex.platform.iam.domain.model.aggregates.User;
import com.molinex.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

public final class UserPersistenceAssembler {
    private UserPersistenceAssembler() {
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        return new User(entity.getId() == null ? null : new UserId(entity.getId()), entity.getEmail(),
                entity.getCredential(), entity.getProfile(), entity.getRole(), entity.getStatus());
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User aggregate) {
        var entity = new UserPersistenceEntity();
        entity.setId(aggregate.getId() == null ? null : aggregate.getId().value());
        entity.setEmail(aggregate.getEmail());
        entity.setCredential(aggregate.getCredential());
        entity.setProfile(aggregate.getProfile());
        entity.setRole(aggregate.getRole());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }
}
