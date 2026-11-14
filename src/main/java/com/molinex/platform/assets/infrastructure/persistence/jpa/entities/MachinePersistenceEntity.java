package com.molinex.platform.assets.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MachineCode;
import com.molinex.platform.assets.domain.model.valueobjects.MachineModel;
import com.molinex.platform.assets.domain.model.valueobjects.MachineName;
import com.molinex.platform.assets.domain.model.valueobjects.MachineStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.MachineCodePersistenceConverter;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.MachineModelPersistenceConverter;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.MachineNamePersistenceConverter;

import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "machines")
@Getter
@Setter
@NoArgsConstructor
public class MachinePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = MachineCodePersistenceConverter.class)
    @Column(nullable = false, unique = true, length = 50)
    private MachineCode code;
    @Convert(converter = MachineNamePersistenceConverter.class)
    @Column(nullable = false, length = 120)
    private MachineName name;
    @Convert(converter = MachineModelPersistenceConverter.class)
    @Column(nullable = false, length = 120)
    private MachineModel model;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MachineStatus status;
    @Column(nullable = true)
    private Instant statusChangedAt;
}
