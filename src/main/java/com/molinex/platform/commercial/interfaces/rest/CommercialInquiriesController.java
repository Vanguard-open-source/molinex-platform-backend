package com.molinex.platform.commercial.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;

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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.molinex.platform.commercial.application.commandservices.CommercialInquiryCommandService;
import com.molinex.platform.commercial.interfaces.rest.resources.CommercialInquiryResource;
import com.molinex.platform.commercial.interfaces.rest.resources.SubmitCommercialInquiryResource;
import com.molinex.platform.commercial.interfaces.rest.transform.CommercialInquiryResourceFromEntityAssembler;
import com.molinex.platform.commercial.interfaces.rest.transform.SubmitCommercialInquiryCommandFromResourceAssembler;

@RestController
@RequestMapping(value = "/api/v1/commercial-inquiries", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "CommercialInquiry")
@RequiredArgsConstructor
public class CommercialInquiriesController {
    private final CommercialInquiryCommandService commands;

    @SecurityRequirements
    @PostMapping("")
    @Operation(summary = "SubmitCommercialInquiry")
    @ApiResponse(responseCode = "201", description = "Resource persisted",
            content = @Content(schema = @Schema(implementation = CommercialInquiryResource.class)))
    public ResponseEntity<?> create(
            @Valid
            @RequestBody
            SubmitCommercialInquiryResource resource) {
        return ResponseEntityAssembler.toResponseEntityFromResult(
                commands.handle(SubmitCommercialInquiryCommandFromResourceAssembler.toCommandFromResource(resource)),
                CommercialInquiryResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.valueOf(201));
    }
}
