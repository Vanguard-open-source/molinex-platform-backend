package com.molinex.platform.production.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "production_batches",
        uniqueConstraints = @UniqueConstraint(name = "uk_production_batch_code", columnNames = "code"))
public class ProductionBatchPersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reception_id", nullable = false)
    private RawMaterialReceptionPersistenceEntity reception;

    @Column(nullable = false)
    private Instant registeredAt;

    protected ProductionBatchPersistenceEntity() {
    }

    public ProductionBatchPersistenceEntity(String code, RawMaterialReceptionPersistenceEntity reception,
                                            Instant registeredAt) {
        this.code = code;
        this.reception = reception;
        this.registeredAt = registeredAt;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Long getReceptionId() {
        return reception.getId();
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }
}
