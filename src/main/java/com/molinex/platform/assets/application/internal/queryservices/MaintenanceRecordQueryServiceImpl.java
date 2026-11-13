package com.molinex.platform.assets.application.internal.queryservices;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

import com.molinex.platform.assets.domain.model.queries.GetMachineMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceRecordByIdQuery;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.application.queryservices.MaintenanceRecordQueryService;
import com.molinex.platform.assets.domain.repositories.MachineRepository;
import com.molinex.platform.assets.domain.repositories.MaintenanceRecordRepository;

@Service
@RequiredArgsConstructor
public class MaintenanceRecordQueryServiceImpl implements MaintenanceRecordQueryService {
    private final MaintenanceRecordRepository repository;

    public Optional<MaintenanceRecord> handle(GetMaintenanceRecordByIdQuery query) {
        return repository.findById(new MaintenanceRecordId(query.id()));
    }

    public List<MaintenanceRecord> handle(GetMaintenanceHistoryQuery query) {
        if (query.machineId() != null) new MachineId(query.machineId());
        return repository.findAll().stream()
                .filter(r -> query.machineId() == null || r.getMachineId().value().equals(query.machineId()))
                .sorted(Comparator.comparing(MaintenanceRecord::getPerformedAt)).toList();
    }

    public Result<List<MaintenanceRecord>, ApplicationError> handle(GetMachineMaintenanceHistoryQuery query) {
        var id = new MachineId(query.machineId());
        if (machines.findById(id).isEmpty())
            return Result.failure(ApplicationError.notFound("Machine", id.value().toString()));
        return Result.success(handle(new GetMaintenanceHistoryQuery(query.machineId())));
    }

    private final MachineRepository machines;
}
