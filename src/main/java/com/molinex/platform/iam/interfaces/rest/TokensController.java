package com.molinex.platform.iam.interfaces.rest;

import lombok.RequiredArgsConstructor;

import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.molinex.platform.iam.application.commandservices.AuthenticationCommandService;
import com.molinex.platform.iam.interfaces.rest.resources.AuthenticateUserResource;
import com.molinex.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.molinex.platform.iam.interfaces.rest.transform.AuthenticateUserCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Tag(name = "Tokens")
public class TokensController {
    private final AuthenticationCommandService commands;

    @PostMapping
    @SecurityRequirements
    @Operation(summary = "Authenticate by email and issue a stateless JWT")
    @ApiResponse(responseCode = "201", description = "Token issued",
            content = @Content(schema = @Schema(implementation = AuthenticatedUserResource.class)))
    public ResponseEntity<?> create(
            @RequestBody
            AuthenticateUserResource resource) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(AuthenticateUserCommandFromResourceAssembler.toCommandFromResource(resource)),
                r -> new AuthenticatedUserResource(r.id(), r.email(), r.role(), r.token()), HttpStatus.CREATED);
    }
}
