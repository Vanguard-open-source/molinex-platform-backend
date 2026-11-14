package com.molinex.platform.production.interfaces.rest;

import com.molinex.platform.production.application.internal.commandservices.ProductionBatchRegistrationService;
import com.molinex.platform.production.application.queryservices.ProductionBatchQueryService;
import com.molinex.platform.production.interfaces.rest.resources.ProductionBatchResource;
import com.molinex.platform.production.interfaces.rest.resources.RegisterProductionBatchResource;
import com.molinex.platform.production.interfaces.rest.transform.ProductionBatchAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/production-batches")
public class ProductionBatchesController {

    private final ProductionBatchRegistrationService commands;
    private final ProductionBatchQueryService queries;

    public ProductionBatchesController(ProductionBatchRegistrationService commands, ProductionBatchQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RegisterProductionBatchResource resource) {
        var result = commands.register(ProductionBatchAssembler.toCommand(resource));
        if (!result.isSuccess()) {
            return ProductionProblems.from(result.error());
        }
        var body = ProductionBatchAssembler.toResource(result.value());
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public List<ProductionBatchResource> getAll() {
        return queries.findAll().stream().map(ProductionBatchAssembler::toResource).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (id <= 0) {
            return ProductionProblems.invalid("invalid-batch-id", "Batch ID must be positive");
        }
        var batch = queries.findById(id);
        if (batch.isEmpty()) {
            return ProductionProblems.notFound("batch-not-found", "Production batch does not exist");
        }
        return ResponseEntity.ok(ProductionBatchAssembler.toResource(batch.get()));
    }
}
