package com.molinex.platform.commercial.infrastructure.persistence.jpa.repositories;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import com.molinex.platform.commercial.infrastructure.persistence.jpa.entities.CommercialInquiryPersistenceEntity;

public interface CommercialInquiryPersistenceRepository extends
        JpaRepository<CommercialInquiryPersistenceEntity, Long> {
}
