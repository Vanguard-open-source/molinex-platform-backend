package com.molinex.platform.assets.interfaces.rest;

import com.molinex.platform.assets.application.queryservices.MaintenanceRecordQueryService;
import com.molinex.platform.assets.domain.model.queries.GetMachineMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceHistoryQuery;
import com.molinex.platform.assets.domain.model.queries.GetMaintenanceRecordByIdQuery;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.assets.domain.model.valueobjects.MaintenanceType;

import com.molinex.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.molinex.platform.shared.application.result.ApplicationError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.molinex.platform.assets.application.commandservices.MaintenanceRecordCommandService;
import com.molinex.platform.assets.interfaces.rest.resources.MaintenanceRecordResource;
import com.molinex.platform.assets.interfaces.rest.resources.RecordMaintenanceResource;
import com.molinex.platform.assets.interfaces.rest.transform.MaintenanceRecordResourceFromEntityAssembler;
import com.molinex.platform.assets.interfaces.rest.transform.RecordCorrectiveMaintenanceCommandFromResourceAssembler;
import com.molinex.platform.assets.interfaces.rest.transform.RecordPreventiveMaintenanceCommandFromResourceAssembler;

@RestController
@RequestMapping(value = "/api/v1/maintenance-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "MaintenanceRecord")
@RequiredArgsConstructor
public class MaintenanceRecordsController {
    private final MaintenanceRecordCommandService commands;

    @PostMapping
    @Operation(summary = "Record preventive or corrective maintenance")
    @ApiResponse(responseCode = "201", description = "Intervention persisted",
            content = @Content(schema = @Schema(implementation = MaintenanceRecordResource.class)))
    public ResponseEntity<?> create(
            @Valid
            @RequestBody
            RecordMaintenanceResource r) {
        if (r.type() == MaintenanceType.CORRECTIVE) return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(RecordCorrectiveMaintenanceCommandFromResourceAssembler.toCommandFromResource(r)),
                MaintenanceRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
        if (r.anomalyId() != null || r.failure() != null || r.cause() != null || r.actionTaken() != null ||
                r.downtimeMinutes() != null)
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                    ApplicationError.validationError("type",
                            "Preventive maintenance cannot carry corrective information"));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(RecordPreventiveMaintenanceCommandFromResourceAssembler.toCommandFromResource(r)),
                MaintenanceRecordResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    private final MaintenanceRecordQueryService queries;

    @GetMapping("/{id}")
    @Operation(summary = "Find MaintenanceRecord by identifier")
    @ApiResponse(responseCode = "200", description = "Resource found",
            content = @Content(schema = @Schema(implementation = MaintenanceRecordResource.class)))
    public ResponseEntity<?> getById(
            @PathVariable
            Long id) {
        var result = queries.handle(new GetMaintenanceRecordByIdQuery(id));
        if (result.isEmpty()) return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("MaintenanceRecord", id.toString()));
        return ResponseEntity.ok(MaintenanceRecordResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }

    @GetMapping
    @Operation(summary = "Maintenance history, optionally for an existing machine")
    @ApiResponse(responseCode = "200", description = "Matching interventions", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = MaintenanceRecordResource.class))))
    public ResponseEntity<?> getAll(
            @RequestParam(required = false)
            Long machineId) {
        if (machineId != null) return ResponseEntityAssembler.toResponseEntityFromResult(
                queries.handle(new GetMachineMaintenanceHistoryQuery(machineId)),
                values -> values.stream().map(MaintenanceRecordResourceFromEntityAssembler::toResourceFromEntity)
                        .toList(), HttpStatus.OK);
        return ResponseEntity.ok(queries.handle(new GetMaintenanceHistoryQuery(null)).stream()
                .map(MaintenanceRecordResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/by-machine/{machineId}")
    @Operation(summary = "Maintenance history of one existing machine")
    @ApiResponse(responseCode = "200", description = "Matching interventions", content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = MaintenanceRecordResource.class))))
    public ResponseEntity<?> byMachine(
            @PathVariable
            Long machineId) {
        return getAll(machineId);
    }
}
