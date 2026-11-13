package com.molinex.platform.assets.infrastructure.persistence.jpa.repositories;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;

import org.springframework.data.jpa.repository.JpaRepository;
import com.molinex.platform.assets.infrastructure.persistence.jpa.entities.MachinePersistenceEntity;

public interface MachinePersistenceRepository extends JpaRepository<MachinePersistenceEntity, Long> {
    boolean existsByCode(MachineCode code);
}
