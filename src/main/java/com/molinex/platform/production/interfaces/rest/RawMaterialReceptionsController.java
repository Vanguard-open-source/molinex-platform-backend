package com.molinex.platform.production.interfaces.rest;

import com.molinex.platform.production.application.commandservices.RawMaterialReceptionCommandService;
import com.molinex.platform.production.application.queryservices.RawMaterialReceptionQueryService;
import com.molinex.platform.production.interfaces.rest.resources.RawMaterialReceptionResource;
import com.molinex.platform.production.interfaces.rest.resources.RecordRawMaterialReceptionResource;
import com.molinex.platform.production.interfaces.rest.transform.RawMaterialReceptionAssembler;
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
@RequestMapping("/api/v1/raw-material-receptions")
public class RawMaterialReceptionsController {

    private final RawMaterialReceptionCommandService commands;
    private final RawMaterialReceptionQueryService queries;

    public RawMaterialReceptionsController(RawMaterialReceptionCommandService commands,
                                           RawMaterialReceptionQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RecordRawMaterialReceptionResource resource) {
        var result = commands.handle(RawMaterialReceptionAssembler.toCommand(resource));
        if (!result.isSuccess()) {
            return ProductionProblems.from(result.error());
        }
        var body = RawMaterialReceptionAssembler.toResource(result.value());
        var location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public List<RawMaterialReceptionResource> getAll() {
        return queries.findAll().stream().map(RawMaterialReceptionAssembler::toResource).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        if (id <= 0) {
            return ProductionProblems.invalid("invalid-reception-id", "Reception ID must be positive");
        }
        var reception = queries.findById(id);
        if (reception.isEmpty()) {
            return ProductionProblems.notFound("reception-not-found", "Raw material reception does not exist");
        }
        return ResponseEntity.ok(RawMaterialReceptionAssembler.toResource(reception.get()));
    }
}
