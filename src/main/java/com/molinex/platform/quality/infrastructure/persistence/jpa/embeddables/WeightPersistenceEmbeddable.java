package com.molinex.platform.quality.infrastructure.persistence.jpa.embeddables;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeightPersistenceEmbeddable {
    @Column(precision = 18, scale = 4)
    private BigDecimal value;
    @Enumerated(EnumType.STRING)
    private MeasurementUnit unit;

    public Weight toDomain() {
        return new Weight(value, unit);
    }

    public static WeightPersistenceEmbeddable fromDomain(Weight value) {
        return new WeightPersistenceEmbeddable(value.value(), value.unit());
    }
}
