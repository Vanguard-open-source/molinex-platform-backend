package com.molinex.platform.iam.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import com.molinex.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Integer> {
    boolean existsByEmail(EmailAddress email);

    Optional<UserPersistenceEntity> findByEmail(EmailAddress email);
}
