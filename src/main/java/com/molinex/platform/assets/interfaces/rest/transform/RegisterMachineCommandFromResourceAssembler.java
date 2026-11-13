package com.molinex.platform.assets.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.commands.RegisterMachineCommand;

import com.molinex.platform.assets.interfaces.rest.resources.RegisterMachineResource;

public final class RegisterMachineCommandFromResourceAssembler {
    private RegisterMachineCommandFromResourceAssembler() {
    }

    public static RegisterMachineCommand toCommandFromResource(RegisterMachineResource resource) {
        return new RegisterMachineCommand(resource.code(), resource.name(), resource.model(), resource.status(),
                resource.statusChangedAt());
    }
}
