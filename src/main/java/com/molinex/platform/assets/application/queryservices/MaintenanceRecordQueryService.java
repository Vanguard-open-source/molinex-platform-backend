package com.molinex.platform.assets.application.queryservices;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.queries.GetMachineMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceRecordByIdQuery;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;

public interface MaintenanceRecordQueryService {
    Optional<MaintenanceRecord> handle(GetMaintenanceRecordByIdQuery query);

    List<MaintenanceRecord> handle(GetMaintenanceHistoryQuery query);

    Result<List<MaintenanceRecord>, ApplicationError> handle(GetMachineMaintenanceHistoryQuery query);
}
