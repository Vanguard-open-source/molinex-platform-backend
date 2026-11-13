package com.molinex.platform.quality.domain.model.aggregates;

import com.molinex.platform.quality.domain.model.events.QualityResultsRecorded;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;
import com.molinex.platform.quality.domain.model.valueobjects.QualityIndicator;
import com.molinex.platform.quality.domain.model.valueobjects.QualityMeasurement;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
public class QualityAssessment extends AbstractDomainAggregateRoot<QualityAssessment> {
    private QualityAssessmentId id;
    private ProductionRecordId productionRecordId;
    private List<QualityMeasurement> measurements;
    private Instant assessedAt;

    public QualityAssessment(QualityAssessmentId id, ProductionRecordId productionRecordId,
            List<QualityMeasurement> measurements, Instant assessedAt) {
        this.id = id;
        if (productionRecordId == null) throw new IllegalArgumentException("productionRecordId is required");
        if (measurements == null) throw new IllegalArgumentException("measurements is required");
        if (assessedAt == null) throw new IllegalArgumentException("assessedAt is required");
        if (measurements.size() != QualityIndicator.values().length ||
                measurements.stream().anyMatch(Objects::isNull) ||
                measurements.stream().map(QualityMeasurement::indicator).distinct().count() !=
                        QualityIndicator.values().length)
            throw new IllegalArgumentException("Exactly one measurement of each quality indicator is required");
        measurements = List.copyOf(measurements);
        this.productionRecordId = productionRecordId;
        this.measurements = measurements;
        this.assessedAt = assessedAt;
    }

    public void assignId(QualityAssessmentId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onCreated() {
        registerDomainEvent(new QualityResultsRecorded(id.value()));
    }

}
