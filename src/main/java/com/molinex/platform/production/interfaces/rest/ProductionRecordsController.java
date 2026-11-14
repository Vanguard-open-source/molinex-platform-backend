package com.molinex.platform.production.interfaces.rest;

import com.molinex.platform.production.application.commandservices.ProductionRecordCommandService;
import com.molinex.platform.production.application.queryservices.ProductionRecordQueryService;
import com.molinex.platform.production.domain.model.queries.GetProductionHistoryQuery;
import com.molinex.platform.production.domain.model.queries.GetProductionProcessesQuery;
import com.molinex.platform.production.interfaces.rest.resources.RecordProductionInformationResource;
import com.molinex.platform.production.interfaces.rest.resources.UpdateProductionInformationResource;
import com.molinex.platform.production.interfaces.rest.transform.ProductionRecordAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/production-records")
public class ProductionRecordsController {

    private final ProductionRecordCommandService commands;
    private final ProductionRecordQueryService queries;

    public ProductionRecordsController(ProductionRecordCommandService commands, ProductionRecordQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RecordProductionInformationResource resource) {
        var result = commands.handle(ProductionRecordAssembler.toCommand(resource));
        if (!result.isSuccess()) {
            return ProductionProblems.from(result.error());
        }
        var body = ProductionRecordAssembler.toResource(result.value());
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody UpdateProductionInformationResource resource) {
        var result = commands.handle(ProductionRecordAssembler.toCommand(id, resource));
        if (!result.isSuccess()) {
            return ProductionProblems.from(result.error());
        }
        return ResponseEntity.ok(ProductionRecordAssembler.toResource(result.value()));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Long batchId) {
        if (batchId != null && batchId <= 0) {
            return ProductionProblems.invalid("invalid-batch-id", "Batch ID must be positive");
        }
        var records = queries.handle(new GetProductionProcessesQuery(batchId));
        return ResponseEntity.ok(records.stream().map(ProductionRecordAssembler::toResource).toList());
    }

    @GetMapping("/history")
    public ResponseEntity<?> history(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        GetProductionHistoryQuery query;
        try {
            query = new GetProductionHistoryQuery(from, to);
        } catch (IllegalArgumentException exception) {
            return ProductionProblems.invalid("invalid-production-period", exception.getMessage());
        }
        return ResponseEntity.ok(queries.handle(query).stream().map(ProductionRecordAssembler::toResource).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (id <= 0) {
            return ProductionProblems.invalid("invalid-production-record-id", "Production record ID must be positive");
        }
        var record = queries.findById(id);
        if (record.isEmpty()) {
            return ProductionProblems.notFound("production-record-not-found", "Production record does not exist");
        }
        return ResponseEntity.ok(ProductionRecordAssembler.toResource(record.get()));
    }
}
