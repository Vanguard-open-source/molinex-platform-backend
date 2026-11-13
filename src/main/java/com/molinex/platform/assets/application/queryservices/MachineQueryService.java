package com.molinex.platform.assets.application.queryservices;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.assets.domain.model.queries.GetAllMachinesQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineByIdQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineStatusQuery;
import com.molinex.platform.assets.domain.model.aggregates.Machine;

public interface MachineQueryService {
    List<Machine> handle(GetAllMachinesQuery query);

    Optional<Machine> handle(GetMachineByIdQuery query);

    Optional<Machine> handle(GetMachineStatusQuery query);
}
