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
import com.molinex.platform.assets.domain.model.aggregates.Machine;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;

public interface MachineRepository {
    Machine save(Machine aggregate);

    Optional<Machine> findById(MachineId id);

    List<Machine> findAll();

    boolean existsByCode(MachineCode code);
}
