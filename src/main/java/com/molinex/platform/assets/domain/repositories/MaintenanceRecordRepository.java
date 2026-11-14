package com.molinex.platform.assets.domain.repositories;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.aggregates.MaintenanceRecord;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;

public interface MaintenanceRecordRepository {
    MaintenanceRecord save(MaintenanceRecord aggregate);

    Optional<MaintenanceRecord> findById(MaintenanceRecordId id);

    List<MaintenanceRecord> findAll();
}
