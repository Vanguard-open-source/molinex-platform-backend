package com.molinex.platform.assets.domain.model.aggregates;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;

import com.molinex.platform.assets.domain.model.events.MachineRegistered;
import com.molinex.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

@Getter
public class Machine extends AbstractDomainAggregateRoot<Machine> {
    private MachineId id;
    private MachineCode code;
    private MachineName name;
    private MachineModel model;
    private MachineStatus status;
    private Instant statusChangedAt;

    public Machine(MachineId id, MachineCode code, MachineName name, MachineModel model, MachineStatus status,
            Instant statusChangedAt) {
        this.id = id;
        if (code == null) throw new IllegalArgumentException("code is required");
        if (name == null) throw new IllegalArgumentException("name is required");
        if (model == null) throw new IllegalArgumentException("model is required");
        if (status == null) throw new IllegalArgumentException("status is required");

        this.code = code;
        this.name = name;
        this.model = model;
        this.status = status;
        this.statusChangedAt = statusChangedAt;
    }

    public void assignId(MachineId id) {
        if (this.id != null && !this.id.equals(id)) throw new IllegalArgumentException("Identity cannot change");
        this.id = Objects.requireNonNull(id);
    }

    public void onCreated() {
        registerDomainEvent(new MachineRegistered(id.value()));
    }

}
