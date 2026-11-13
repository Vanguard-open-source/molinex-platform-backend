package com.molinex.platform.quality.interfaces.rest;

import com.molinex.platform.quality.application.commandservices.QualityAssessmentCommandService;
import com.molinex.platform.quality.application.queryservices.QualityAssessmentQueryService;
import com.molinex.platform.quality.domain.model.queries.GetAllQualityResultsQuery;
import com.molinex.platform.quality.domain.model.queries.GetQualityAssessmentByIdQuery;
import com.molinex.platform.quality.interfaces.rest.resources.QualityAssessmentResource;
import com.molinex.platform.quality.interfaces.rest.resources.RecordQualityResultsResource;
import com.molinex.platform.quality.interfaces.rest.transform.QualityAssessmentResourceFromEntityAssembler;
import com.molinex.platform.quality.interfaces.rest.transform.RecordQualityResultsCommandFromResourceAssembler;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/quality-results", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "QualityAssessment")
@RequiredArgsConstructor
public class QualityAssessmentsController {
    private final QualityAssessmentCommandService commands;

    @PostMapping("")
    @Operation(summary = "RecordQualityResults")
    @ApiResponse(responseCode = "201", description = "Resource persisted",
            content = @Content(schema = @Schema(implementation = QualityAssessmentResource.class)))
    public ResponseEntity<?> create(
            @Valid
            @RequestBody
            RecordQualityResultsResource resource) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(RecordQualityResultsCommandFromResourceAssembler.toCommandFromResource(resource)),
                QualityAssessmentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.valueOf(201));
    }

    private final QualityAssessmentQueryService queries;

    @GetMapping
    @Operation(summary = "List quality-results")
    @ApiResponse(responseCode = "200", description = "Resource collection", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = QualityAssessmentResource.class))))
    public ResponseEntity<List<QualityAssessmentResource>> getAll() {
        return ResponseEntity.ok(queries.handle(new GetAllQualityResultsQuery()).stream()
                .map(QualityAssessmentResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find QualityAssessment by identifier")
    @ApiResponse(responseCode = "200", description = "Resource found",
            content = @Content(schema = @Schema(implementation = QualityAssessmentResource.class)))
    public ResponseEntity<?> getById(
            @PathVariable
            Integer id) {
        var result = queries.handle(new GetQualityAssessmentByIdQuery(id));
        if (result.isEmpty()) return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("QualityAssessment", id.toString()));
        return ResponseEntity.ok(QualityAssessmentResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }
}
