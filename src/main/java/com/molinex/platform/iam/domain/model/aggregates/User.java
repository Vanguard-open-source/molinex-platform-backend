package com.molinex.platform.iam.domain.model.aggregates;

import java.util.Objects;

import lombok.Getter;
import com.molinex.platform.iam.domain.model.valueobjects.AccountStatus;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.valueobjects.PasswordHash;
import com.molinex.platform.iam.domain.model.valueobjects.Role;
import com.molinex.platform.iam.domain.model.valueobjects.UserId;
import com.molinex.platform.iam.domain.model.valueobjects.UserProfile;

import com.molinex.platform.iam.domain.model.events.UserAuthenticated;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

@Getter
public class User extends AbstractDomainAggregateRoot<User> {
    private UserId id;
    private EmailAddress email;
    private PasswordHash credential;
    private UserProfile profile;
    private Role role;
    private AccountStatus status;

    public User(UserId id, EmailAddress email, PasswordHash credential, UserProfile profile, Role role,
            AccountStatus status) {
        this.id = id;
        if (email == null) throw new IllegalArgumentException("email is required");
        if (credential == null) throw new IllegalArgumentException("credential is required");
        if (profile == null) throw new IllegalArgumentException("profile is required");
        if (role == null) throw new IllegalArgumentException("role is required");
        if (status == null) throw new IllegalArgumentException("status is required");

        this.email = email;
        this.credential = credential;
        this.profile = profile;
        this.role = role;
        this.status = status;
    }

    public void assignId(UserId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onAuthenticated() {
        registerDomainEvent(new UserAuthenticated(id.value()));
    }
}
