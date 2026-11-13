package com.molinex.platform.assets.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.commands.RecordCorrectiveMaintenanceCommand;

import com.molinex.platform.assets.interfaces.rest.resources.RecordMaintenanceResource;

public final class RecordCorrectiveMaintenanceCommandFromResourceAssembler {
    private RecordCorrectiveMaintenanceCommandFromResourceAssembler() {
    }

    public static RecordCorrectiveMaintenanceCommand toCommandFromResource(RecordMaintenanceResource r) {
        return new RecordCorrectiveMaintenanceCommand(r.machineId(), r.performedAt(), r.description(),
                r.responsiblePrincipalId(), r.responsibleDisplayName(), r.anomalyId(), r.failure(), r.cause(),
                r.actionTaken(), r.downtimeMinutes());
    }
}
