package com.molinex.platform.assets.infrastructure.persistence.jpa.entities;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.AnomalyReference;
import com.molinex.platform.assets.domain.model.valueobjects.MachineId;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceDescription;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.AnomalyReferencePersistenceConverter;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.MachineIdPersistenceConverter;
import com.molinex.platform.assets.infrastructure.persistence.jpa.converters.MaintenanceDescriptionPersistenceConverter;
import com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables
        .CorrectiveMaintenanceDetailsPersistenceEmbeddable;
import com.molinex.platform.assets.infrastructure.persistence.jpa.embeddables.TechnicianReferencePersistenceEmbeddable;
import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = MachineIdPersistenceConverter.class)
    @Column(nullable = false)
    private MachineId machineId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceType type;
    @Column(nullable = false)
    private Instant performedAt;
    @Convert(converter = MaintenanceDescriptionPersistenceConverter.class)
    @Column(nullable = false, length = 2000)
    private MaintenanceDescription description;
    @Embedded
    private TechnicianReferencePersistenceEmbeddable responsible;
    @Convert(converter = AnomalyReferencePersistenceConverter.class)
    @Column(nullable = true)
    private AnomalyReference anomalyId;
    @Embedded
    private CorrectiveMaintenanceDetailsPersistenceEmbeddable correctiveDetails;
}
