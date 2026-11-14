package com.molinex.platform.production.domain.model.aggregates;

import com.molinex.platform.production.domain.model.valueobjects.MaterialOrigin;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.model.valueobjects.Supplier;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;

import java.time.Instant;
import java.util.Objects;

public class RawMaterialReception {

    private RawMaterialReceptionId id;
    private final Instant receivedAt;
    private final Supplier supplier;
    private final MaterialOrigin origin;
    private final Weight quantity;

    public RawMaterialReception(RawMaterialReceptionId id, Instant receivedAt, Supplier supplier,
                                MaterialOrigin origin, Weight quantity) {
        if (receivedAt == null || supplier == null || origin == null || quantity == null) {
            throw new IllegalArgumentException("Reception date, supplier, origin and weight are required");
        }
        if (quantity.value().signum() <= 0) {
            throw new IllegalArgumentException("Reception weight must be positive");
        }
        this.id = id;
        this.receivedAt = receivedAt;
        this.supplier = supplier;
        this.origin = origin;
        this.quantity = quantity;
    }

    public void assignId(RawMaterialReceptionId id) {
        if (this.id != null && !this.id.equals(id)) {
            throw new IllegalArgumentException("Identity cannot change");
        }
        this.id = Objects.requireNonNull(id);
    }

    public RawMaterialReceptionId getId() {
        return id;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public MaterialOrigin getOrigin() {
        return origin;
    }

    public Weight getQuantity() {
        return quantity;
    }
}
