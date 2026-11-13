package com.molinex.platform.assets.domain.model.queries;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;





public record GetMachineByIdQuery(
        Long id
) {
}
