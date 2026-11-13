package com.molinex.platform.quality.infrastructure.persistence.jpa.entities;

import com.molinex.platform.quality.domain.model.valueobjects.Percentage;
import com.molinex.platform.quality.domain.model.valueobjects.QualityIndicator;
import com.molinex.platform.quality.domain.model.valueobjects.QualityMeasurement;
import com.molinex.platform.quality.domain.model.valueobjects.QualityRange;
import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Owned persistence entity; domain measurements remain immutable value objects.
 */
@Entity
@Table(name = "quality_measurements",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assessment_id", "indicator"}))
@Getter
@Setter
@NoArgsConstructor
public class QualityMeasurementPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private QualityAssessmentPersistenceEntity assessment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualityIndicator indicator;
    @Column(name = "value_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal value;
    @Column(name = "expected_min_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal expectedMinimum;
    @Column(name = "expected_max_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal expectedMaximum;

    public QualityMeasurement toDomain() {
        return new QualityMeasurement(indicator, new Percentage(value),
                new QualityRange(new Percentage(expectedMinimum), new Percentage(expectedMaximum)));
    }

    public static QualityMeasurementPersistenceEntity fromDomain(QualityMeasurement measurement) {
        var entity = new QualityMeasurementPersistenceEntity();
        entity.setIndicator(measurement.indicator());
        entity.setValue(measurement.value().value());
        entity.setExpectedMinimum(measurement.expectedRange().minimum().value());
        entity.setExpectedMaximum(measurement.expectedRange().maximum().value());
        return entity;
    }
}
