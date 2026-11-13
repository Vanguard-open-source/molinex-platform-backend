package com.molinex.platform.assets.application.commandservices;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.commands.RecordCorrectiveMaintenanceCommand;
import com.molinex.platform.assets.domain.model.commands.RecordPreventiveMaintenanceCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;

public interface MaintenanceRecordCommandService {
    Result<MaintenanceRecord, ApplicationError> handle(RecordPreventiveMaintenanceCommand command);

    Result<MaintenanceRecord, ApplicationError> handle(RecordCorrectiveMaintenanceCommand command);
}
