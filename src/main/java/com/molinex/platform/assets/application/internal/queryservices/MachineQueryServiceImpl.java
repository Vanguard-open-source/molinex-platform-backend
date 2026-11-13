package com.molinex.platform.assets.application.internal.queryservices;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;

import com.molinex.platform.assets.domain.model.queries.GetAllMachinesQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineByIdQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineStatusQuery;
import com.molinex.platform.assets.domain.model.aggregates.Machine;
import org.springframework.stereotype.Service;
import com.molinex.platform.assets.application.queryservices.MachineQueryService;
import com.molinex.platform.assets.domain.repositories.MachineRepository;

@Service
@RequiredArgsConstructor
public class MachineQueryServiceImpl implements MachineQueryService {
    private final MachineRepository repository;

    public List<Machine> handle(GetAllMachinesQuery query) {
        return repository.findAll();
    }

    public Optional<Machine> handle(GetMachineByIdQuery query) {
        return repository.findById(new MachineId(query.id()));
    }

    public Optional<Machine> handle(GetMachineStatusQuery query) {
        return repository.findById(new MachineId(query.id()));
    }
}
