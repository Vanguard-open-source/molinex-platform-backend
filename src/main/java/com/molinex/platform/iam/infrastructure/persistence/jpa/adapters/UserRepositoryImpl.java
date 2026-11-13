package com.molinex.platform.iam.infrastructure.persistence.jpa.adapters;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.valueobjects.UserId;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.molinex.platform.iam.domain.model.aggregates.User;
import com.molinex.platform.iam.domain.repositories.UserRepository;
import com.molinex.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import com.molinex.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserRepositoryImpl implements UserRepository {
    private final UserPersistenceRepository persistence;

    public User save(User aggregate) {
        var entity = persistence.saveAndFlush(UserPersistenceAssembler.toPersistenceFromDomain(aggregate));
        aggregate.assignId(new UserId(entity.getId()));
        return aggregate;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return persistence.findById(id.value()).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return persistence.findAll().stream().map(UserPersistenceAssembler::toDomainFromPersistence).toList();
    }

    public boolean existsByEmail(EmailAddress email) {
        return persistence.existsByEmail(email);
    }

    public Optional<User> findByEmail(EmailAddress email) {
        return persistence.findByEmail(email).map(UserPersistenceAssembler::toDomainFromPersistence);
    }
}
