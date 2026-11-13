package com.molinex.platform.commercial.infrastructure.persistence.jpa.embeddables;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.ContactInformation;
import com.molinex.platform.commercial.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.commercial.domain.model.valueobjects.PhoneNumber;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformationPersistenceEmbeddable {
    @Column(length = 150, nullable = false)
    private String fullName;
    @Column(length = 254, nullable = false)
    private String email;
    @Column(length = 30, nullable = true)
    private String phoneNumber;
    @Column(length = 150, nullable = false)
    private String companyName;

    public ContactInformation toDomain() {
        return new ContactInformation(fullName, new EmailAddress(email),
                phoneNumber == null ? null : new PhoneNumber(phoneNumber), companyName);
    }

    public static ContactInformationPersistenceEmbeddable fromDomain(ContactInformation value) {
        return new ContactInformationPersistenceEmbeddable(value.fullName(), value.email().value(),
                value.phoneNumber() == null ? null : value.phoneNumber().value(), value.companyName());
    }
}
