package com.molinex.platform.quality.domain.model.aggregates;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;
import com.molinex.platform.quality.domain.model.events.ProductionWasteRecorded;
import com.molinex.platform.quality.domain.model.valueobjects.Percentage;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

@Getter
public class WasteRecord extends AbstractDomainAggregateRoot<WasteRecord> {
    private WasteRecordId id;
    private ProductionRecordId productionRecordId;
    private Weight quantity;
    private Weight baseWeight;
    private Percentage percentage;
    private Instant recordedAt;

    public WasteRecord(WasteRecordId id, ProductionRecordId productionRecordId, Weight quantity, Weight baseWeight,
            Percentage percentage, Instant recordedAt) {
        this.id = id;
        if (productionRecordId == null) throw new IllegalArgumentException("productionRecordId is required");
        if (quantity == null) throw new IllegalArgumentException("quantity is required");
        if (recordedAt == null) throw new IllegalArgumentException("recordedAt is required");
        if (!quantity.isNonNegative()) throw new IllegalArgumentException("quantityValue must not be negative");
        if (baseWeight != null &&
                (!baseWeight.isPositive() || quantity.inKilograms().compareTo(baseWeight.inKilograms()) > 0))
            throw new IllegalArgumentException("baseWeight must be positive and not less than waste");
        percentage = baseWeight == null ? null : new Percentage(quantity.inKilograms().multiply(new BigDecimal("100"))
                .divide(baseWeight.inKilograms(), 2, RoundingMode.HALF_UP));
        this.productionRecordId = productionRecordId;
        this.quantity = quantity;
        this.baseWeight = baseWeight;
        this.percentage = percentage;
        this.recordedAt = recordedAt;
    }

    public void assignId(WasteRecordId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onCreated() {
        registerDomainEvent(new ProductionWasteRecorded(id.value()));
    }

}
