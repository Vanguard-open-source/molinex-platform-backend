package com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.CorrectiveMaintenanceDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorrectiveMaintenanceDetailsPersistenceEmbeddable {
    @Column(length = 2000, nullable = true)
    private String failure;
    @Column(length = 2000, nullable = true)
    private String cause;
    @Column(length = 2000, nullable = true)
    private String actionTaken;
    private Integer downtimeMinutes;

    public CorrectiveMaintenanceDetails toDomain() {
        return new CorrectiveMaintenanceDetails(failure, cause, actionTaken, downtimeMinutes);
    }

    public static CorrectiveMaintenanceDetailsPersistenceEmbeddable fromDomain(CorrectiveMaintenanceDetails value) {
        return new CorrectiveMaintenanceDetailsPersistenceEmbeddable(value.failure(), value.cause(),
                value.actionTaken(), value.downtimeMinutes());
    }
}
