package com.molinex.platform.quality.infrastructure.persistence.jpa.entities;

import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.infrastructure.persistence.jpa.converters.ProductionRecordIdPersistenceConverter;
import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "quality_assessments")
@Getter
@Setter
@NoArgsConstructor
public class QualityAssessmentPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Convert(converter = ProductionRecordIdPersistenceConverter.class)
    @Column(nullable = false)
    private ProductionRecordId productionRecordId;
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QualityMeasurementPersistenceEntity> measurements;
    @Column(nullable = false)
    private Instant assessedAt;
}
