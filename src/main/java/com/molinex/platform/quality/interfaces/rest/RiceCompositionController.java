package com.molinex.platform.quality.interfaces.rest;

import com.molinex.platform.quality.application.queryservices.RiceCompositionQueryService;
import com.molinex.platform.quality.domain.model.queries.GetRiceCompositionQuery;
import com.molinex.platform.quality.interfaces.rest.resources.RiceCompositionResource;
import com.molinex.platform.quality.interfaces.rest.transform.QualityAssessmentResourceFromEntityAssembler;
import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rice-composition")
@RequiredArgsConstructor
public class RiceCompositionController {
    private final RiceCompositionQueryService queries;

    @GetMapping
    @Operation(summary = "Recorded rice composition for one batch; no averaging")
    @ApiResponse(responseCode = "200", description = "Composition availability and observations",
            content = @Content(schema = @Schema(implementation = RiceCompositionResource.class)))
    public ResponseEntity<?> get(
            @RequestParam
            Long batchId) {
        return ResponseEntityAssembler.toResponseEntityFromResult(queries.handle(new GetRiceCompositionQuery(batchId)),
                values -> new RiceCompositionResource(batchId, !values.isEmpty(),
                        values.isEmpty() ? "No quality results for this batch" : null,
                        values.stream().map(QualityAssessmentResourceFromEntityAssembler::toResourceFromEntity)
                                .toList()), HttpStatus.OK);
    }
}
