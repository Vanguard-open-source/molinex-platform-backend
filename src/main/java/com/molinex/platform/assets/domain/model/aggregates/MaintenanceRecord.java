package com.molinex.platform.assets.domain.model.aggregates;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.AnomalyReference;
import com.molinex.platform.assets.domain.model.valueobjects.CorrectiveMaintenanceDetails;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceDescription;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceRecordId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;
import com.molinex.platform.assets.domain.model.valueobjects.TechnicianReference;

import com.molinex.platform.assets.domain.model.events.CorrectiveMaintenanceRecorded;
import com.molinex.platform.assets.domain.model.events.PreventiveMaintenanceRecorded;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

@Getter
public class MaintenanceRecord extends AbstractDomainAggregateRoot<MaintenanceRecord> {
    private MaintenanceRecordId id;
    private MachineId machineId;
    private MaintenanceType type;
    private Instant performedAt;
    private MaintenanceDescription description;
    private TechnicianReference responsible;
    private AnomalyReference anomalyId;
    private CorrectiveMaintenanceDetails correctiveDetails;

    public MaintenanceRecord(MaintenanceRecordId id, MachineId machineId, MaintenanceType type, Instant performedAt,
            MaintenanceDescription description, TechnicianReference responsible, AnomalyReference anomalyId,
            CorrectiveMaintenanceDetails correctiveDetails) {
        this.id = id;
        if (machineId == null) throw new IllegalArgumentException("machineId is required");
        if (type == null) throw new IllegalArgumentException("type is required");
        if (performedAt == null) throw new IllegalArgumentException("performedAt is required");
        if (description == null) throw new IllegalArgumentException("description is required");
        if (responsible == null) throw new IllegalArgumentException("responsible is required");
        if (type == MaintenanceType.CORRECTIVE && correctiveDetails == null)
            throw new IllegalArgumentException("Corrective failure and action information is required");
        if (type == MaintenanceType.PREVENTIVE && (correctiveDetails != null || anomalyId != null))
            throw new IllegalArgumentException("Preventive maintenance cannot carry corrective information");
        this.machineId = machineId;
        this.type = type;
        this.performedAt = performedAt;
        this.description = description;
        this.responsible = responsible;
        this.anomalyId = anomalyId;
        this.correctiveDetails = correctiveDetails;
    }

    public void assignId(MaintenanceRecordId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onCreated() {
        if (type == MaintenanceType.PREVENTIVE) registerDomainEvent(new PreventiveMaintenanceRecorded(id.value()));
        else registerDomainEvent(new CorrectiveMaintenanceRecorded(id.value()));
    }

}
