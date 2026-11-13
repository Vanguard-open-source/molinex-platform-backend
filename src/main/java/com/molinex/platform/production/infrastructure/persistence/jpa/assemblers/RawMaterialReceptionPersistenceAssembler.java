package com.molinex.platform.production.infrastructure.persistence.jpa.assemblers;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.valueobjects.MaterialOrigin;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.model.valueobjects.Supplier;
import com.molinex.platform.production.infrastructure.persistence.jpa.entities.RawMaterialReceptionPersistenceEntity;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.MeasurementUnit;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;

public final class RawMaterialReceptionPersistenceAssembler {

    private RawMaterialReceptionPersistenceAssembler() {
    }

    public static RawMaterialReceptionPersistenceEntity toEntity(RawMaterialReception reception) {
        var entity = new RawMaterialReceptionPersistenceEntity();
        if (reception.getId() != null) {
            entity.setId(reception.getId().value());
        }
        entity.setReceivedAt(reception.getReceivedAt());
        entity.setSupplierName(reception.getSupplier().name());
        entity.setOriginDescription(reception.getOrigin().description());
        entity.setQuantityValue(reception.getQuantity().value());
        entity.setQuantityUnit(reception.getQuantity().unit().name());
        return entity;
    }

    public static RawMaterialReception toDomain(RawMaterialReceptionPersistenceEntity entity) {
        return new RawMaterialReception(new RawMaterialReceptionId(entity.getId()), entity.getReceivedAt(),
                new Supplier(entity.getSupplierName()), new MaterialOrigin(entity.getOriginDescription()),
                new Weight(entity.getQuantityValue(), MeasurementUnit.valueOf(entity.getQuantityUnit())));
    }
}
