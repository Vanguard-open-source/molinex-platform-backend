package com.molinex.platform.production.infrastructure.persistence.jpa.entities;

import com.molinex.platform.production.domain.model.valueobjects.ProductionStatus;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "production_records", indexes = @Index(name = "ix_production_records_recorded_at", columnList = "recorded_at"))
public class ProductionRecordPersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false, updatable = false)
    private ProductionBatchPersistenceEntity batch;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private Instant recordedAt;

    @Column(nullable = false, length = 120)
    private String processName;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal processedWeightValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeasurementUnit processedWeightUnit;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductionStatus status;

    protected ProductionRecordPersistenceEntity() {
    }

    public ProductionRecordPersistenceEntity(ProductionBatchPersistenceEntity batch, Instant recordedAt) {
        this.batch = batch;
        this.recordedAt = recordedAt;
    }

    public void updateDetails(String processName, BigDecimal weight, MeasurementUnit unit, Instant startedAt,
                              Instant finishedAt, ProductionStatus status) {
        this.processName = processName;
        this.processedWeightValue = weight;
        this.processedWeightUnit = unit;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getBatchId() {
        return batch.getId();
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public String getProcessName() {
        return processName;
    }

    public BigDecimal getProcessedWeightValue() {
        return processedWeightValue;
    }

    public MeasurementUnit getProcessedWeightUnit() {
        return processedWeightUnit;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public ProductionStatus getStatus() {
        return status;
    }
}
