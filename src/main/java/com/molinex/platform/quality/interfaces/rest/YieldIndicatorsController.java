package com.molinex.platform.quality.interfaces.rest;

import com.molinex.platform.quality.application.queryservices.YieldIndicatorQueryService;
import com.molinex.platform.quality.domain.model.queries.GetYieldIndicatorsQuery;
import com.molinex.platform.quality.interfaces.rest.resources.IndicatorSetResource;
import com.molinex.platform.quality.interfaces.rest.transform.IndicatorResourceFromValueAssembler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/yield-indicators")
@RequiredArgsConstructor
public class YieldIndicatorsController {
    private final YieldIndicatorQueryService queries;

    @GetMapping
    @Operation(summary = "Recorded quality and calculated waste indicators with explicit availability")
    public IndicatorSetResource get(
            @RequestParam(required = false)
            Instant from,
            @RequestParam(required = false)
            Instant to,
            @RequestParam(required = false)
            Long productionRecordId) {
        return new IndicatorSetResource(from, to, productionRecordId,
                queries.handle(new GetYieldIndicatorsQuery(from, to, productionRecordId)).stream()
                        .map(IndicatorResourceFromValueAssembler::toResourceFromValue).toList());
    }
}
