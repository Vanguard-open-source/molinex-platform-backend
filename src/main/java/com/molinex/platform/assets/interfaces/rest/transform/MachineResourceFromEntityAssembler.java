package com.molinex.platform.assets.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.interfaces.rest.resources.MachineResource;

public final class MachineResourceFromEntityAssembler {
    private MachineResourceFromEntityAssembler() {
    }

    public static MachineResource toResourceFromEntity(Machine entity) {
        return new MachineResource(entity.getId().value(), entity.getCode().value(), entity.getName().value(),
                entity.getModel().value(), entity.getStatus(), entity.getStatusChangedAt());
    }
}
