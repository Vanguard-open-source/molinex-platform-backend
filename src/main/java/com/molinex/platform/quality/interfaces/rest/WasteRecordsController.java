package com.molinex.platform.quality.interfaces.rest;

import com.molinex.platform.quality.application.commandservices.WasteRecordCommandService;
import com.molinex.platform.quality.application.queryservices.WasteRecordQueryService;
import com.molinex.platform.quality.domain.model.queries.GetProductionWasteQuery;
import com.molinex.platform.quality.domain.model.queries.GetWasteRecordByIdQuery;
import com.molinex.platform.quality.interfaces.rest.resources.RecordProductionWasteResource;
import com.molinex.platform.quality.interfaces.rest.resources.WasteRecordResource;
import com.molinex.platform.quality.interfaces.rest.transform.RecordProductionWasteCommandFromResourceAssembler;
import com.molinex.platform.quality.interfaces.rest.transform.WasteRecordResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/waste-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "WasteRecord")
@RequiredArgsConstructor
public class WasteRecordsController {
    private final WasteRecordCommandService commands;

    @PostMapping("")
    @Operation(summary = "RecordProductionWaste")
    @ApiResponse(responseCode = "201", description = "Resource persisted",
            content = @Content(schema = @Schema(implementation = WasteRecordResource.class)))
    public ResponseEntity<?> create(
            @Valid
            @RequestBody
            RecordProductionWasteResource resource) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(RecordProductionWasteCommandFromResourceAssembler.toCommandFromResource(resource)),
                WasteRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.valueOf(201));
    }

    private final WasteRecordQueryService queries;

    @GetMapping("/{id}")
    @Operation(summary = "Find WasteRecord by identifier")
    @ApiResponse(responseCode = "200", description = "Resource found",
            content = @Content(schema = @Schema(implementation = WasteRecordResource.class)))
    public ResponseEntity<?> getById(
            @PathVariable
            Integer id) {
        var result = queries.handle(new GetWasteRecordByIdQuery(id));
        if (result.isEmpty()) return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("WasteRecord", id.toString()));
        return ResponseEntity.ok(WasteRecordResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }

    @GetMapping
    @Operation(summary = "List waste, optionally by production record")
    @ApiResponse(responseCode = "200", description = "Waste records",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WasteRecordResource.class))))
    public ResponseEntity<List<WasteRecordResource>> getAll(
            @RequestParam(required = false)
            Integer productionRecordId) {
        return ResponseEntity.ok(queries.handle(new GetProductionWasteQuery(productionRecordId)).stream()
                .map(WasteRecordResourceFromEntityAssembler::toResourceFromEntity).toList());
    }
}
