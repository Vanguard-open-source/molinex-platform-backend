package com.molinex.platform.commercial.domain.repositories;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.domain.model.valueobjects.CommercialInquiryId;

public interface CommercialInquiryRepository {
    CommercialInquiry save(CommercialInquiry aggregate);

    Optional<CommercialInquiry> findById(CommercialInquiryId id);

    List<CommercialInquiry> findAll();
}
