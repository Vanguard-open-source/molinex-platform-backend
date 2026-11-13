package com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.TechnicianReference;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianReferencePersistenceEmbeddable {
    @Column(nullable = false)
    private Long principalId;
    @Column(length = 150, nullable = false)
    private String displayName;

    public TechnicianReference toDomain() {
        return new TechnicianReference(principalId, displayName);
    }

    public static TechnicianReferencePersistenceEmbeddable fromDomain(TechnicianReference value) {
        return new TechnicianReferencePersistenceEmbeddable(value.principalId(), value.displayName());
    }
}
