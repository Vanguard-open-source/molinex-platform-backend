
package com.molinex.platform.production.infrastructure.persistence.jpa.entities;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "raw_material_receptions")
public class RawMaterialReceptionPersistenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant receivedAt;

    @Column(nullable = false, length = 150)
    private String supplierName;

    @Column(nullable = false, length = 255)
    private String originDescription;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal quantityValue;

    @Column(nullable = false, length = 30)
    private String quantityUnit;

    public RawMaterialReceptionPersistenceEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getOriginDescription() {
        return originDescription;
    }

    public void setOriginDescription(String originDescription) {
        this.originDescription = originDescription;
    }

    public BigDecimal getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(BigDecimal quantityValue) {
        this.quantityValue = quantityValue;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }
}
