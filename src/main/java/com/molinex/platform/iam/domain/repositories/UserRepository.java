package com.molinex.platform.iam.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.molinex.platform.iam.domain.model.aggregates.User;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.valueobjects.UserId;

public interface UserRepository {
    User save(User aggregate);

    Optional<User> findById(UserId id);

    List<User> findAll();

    boolean existsByEmail(EmailAddress email);

    Optional<User> findByEmail(EmailAddress email);
}
