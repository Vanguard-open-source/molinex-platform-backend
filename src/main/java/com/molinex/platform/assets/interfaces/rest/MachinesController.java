package com.molinex.platform.assets.interfaces.rest;

import com.molinex.platform.assets.application.queryservices.MachineQueryService;
import com.molinex.platform.assets.domain.model.queries.GetAllMachinesQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineByIdQuery;
import com.molinex.platform.assets.domain.model.queries.GetMachineStatusQuery;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.molinex.platform.shared.application.result.ApplicationError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.molinex.platform.assets.application.commandservices.MachineCommandService;
import com.molinex.platform.assets.interfaces.rest.resources.MachineResource;
import com.molinex.platform.assets.interfaces.rest.resources.MachineStatusResource;
import com.molinex.platform.assets.interfaces.rest.resources.RegisterMachineResource;
import com.molinex.platform.assets.interfaces.rest.transform.MachineResourceFromEntityAssembler;
import com.molinex.platform.assets.interfaces.rest.transform.RegisterMachineCommandFromResourceAssembler;

@RestController
@RequestMapping(value = "/api/v1/machines", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Machine")
@RequiredArgsConstructor
public class MachinesController {
    private final MachineCommandService commands;

    @PostMapping("")
    @Operation(summary = "RegisterMachine")
    @ApiResponse(responseCode = "201", description = "Resource persisted",
            content = @Content(schema = @Schema(implementation = MachineResource.class)))
    public ResponseEntity<?> create(
            @Valid
            @RequestBody
            RegisterMachineResource resource) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(RegisterMachineCommandFromResourceAssembler.toCommandFromResource(resource)),
                MachineResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.valueOf(201));
    }

    private final MachineQueryService queries;

    @GetMapping
    @Operation(summary = "List machines")
    @ApiResponse(responseCode = "200", description = "Resource collection",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MachineResource.class))))
    public ResponseEntity<List<MachineResource>> getAll() {
        return ResponseEntity.ok(queries.handle(new GetAllMachinesQuery()).stream()
                .map(MachineResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find Machine by identifier")
    @ApiResponse(responseCode = "200", description = "Resource found",
            content = @Content(schema = @Schema(implementation = MachineResource.class)))
    public ResponseEntity<?> getById(
            @PathVariable
            Long id) {
        var result = queries.handle(new GetMachineByIdQuery(id));
        if (result.isEmpty()) return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("Machine", id.toString()));
        return ResponseEntity.ok(MachineResourceFromEntityAssembler.toResourceFromEntity(result.get()));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Machine state and last update, with pending flag for legacy data")
    public ResponseEntity<?> status(
            @PathVariable
            Long id) {
        var machine = queries.handle(new GetMachineStatusQuery(id));
        if (machine.isEmpty()) return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.notFound("Machine", id.toString()));
        var m = machine.get();
        return ResponseEntity.ok(
                new MachineStatusResource(id, m.getStatus(), m.getStatusChangedAt(), m.getStatusChangedAt() == null));
    }
}
