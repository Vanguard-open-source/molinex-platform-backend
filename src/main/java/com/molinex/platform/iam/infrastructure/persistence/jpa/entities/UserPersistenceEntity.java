package com.molinex.platform.iam.infrastructure.persistence.jpa.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.molinex.platform.iam.domain.model.valueobjects.AccountStatus;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.valueobjects.PasswordHash;
import com.molinex.platform.iam.domain.model.valueobjects.Role;
import com.molinex.platform.iam.domain.model.valueobjects.UserProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.molinex.platform.iam.infrastructure.persistence.jpa.converters.EmailAddressPersistenceConverter;
import com.molinex.platform.iam.infrastructure.persistence.jpa.converters.PasswordHashPersistenceConverter;
import com.molinex.platform.iam.infrastructure.persistence.jpa.converters.UserProfilePersistenceConverter;

import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Convert(converter = EmailAddressPersistenceConverter.class)
    @Column(nullable = false, unique = true, length = 254)
    private EmailAddress email;
    @Convert(converter = PasswordHashPersistenceConverter.class)
    @Column(nullable = false)
    private PasswordHash credential;
    @Convert(converter = UserProfilePersistenceConverter.class)
    @Column(nullable = false, length = 150)
    private UserProfile profile;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;
}
