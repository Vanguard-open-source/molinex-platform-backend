package com.molinex.platform.assets.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.commands.RecordPreventiveMaintenanceCommand;

import com.molinex.platform.assets.interfaces.rest.resources.RecordMaintenanceResource;

public final class RecordPreventiveMaintenanceCommandFromResourceAssembler {
    private RecordPreventiveMaintenanceCommandFromResourceAssembler() {
    }

    public static RecordPreventiveMaintenanceCommand toCommandFromResource(RecordMaintenanceResource r) {
        return new RecordPreventiveMaintenanceCommand(r.machineId(), r.performedAt(), r.description(),
                r.responsiblePrincipalId(), r.responsibleDisplayName());
    }
}
