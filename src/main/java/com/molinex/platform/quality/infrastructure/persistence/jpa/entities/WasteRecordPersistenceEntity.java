package com.molinex.platform.quality.infrastructure.persistence.jpa.entities;

import com.molinex.platform.quality.domain.model.valueobjects.Percentage;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.infrastructure.persistence.jpa.converters.PercentagePersistenceConverter;
import com.molinex.platform.quality.infrastructure.persistence.jpa.converters.ProductionRecordIdPersistenceConverter;
import com.molinex.platform.quality.infrastructure.persistence.jpa.embeddables.WeightPersistenceEmbeddable;
import com.molinex.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "waste_records")
@Getter
@Setter
@NoArgsConstructor
public class WasteRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Convert(converter = ProductionRecordIdPersistenceConverter.class)
    @Column(nullable = false)
    private ProductionRecordId productionRecordId;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value",
            column = @Column(name = "quantity_value", precision = 18, scale = 4, nullable = false)),
            @AttributeOverride(name = "unit", column = @Column(name = "quantity_unit", nullable = false))})
    private WeightPersistenceEmbeddable quantity;
    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value",
            column = @Column(name = "base_weight_value", precision = 18, scale = 4)),
            @AttributeOverride(name = "unit", column = @Column(name = "base_weight_unit"))})
    private WeightPersistenceEmbeddable baseWeight;
    @Convert(converter = PercentagePersistenceConverter.class)
    @Column(nullable = true, precision = 5, scale = 2)
    private Percentage percentage;
    @Column(nullable = false)
    private Instant recordedAt;
}
