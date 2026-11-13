package com.molinex.platform.commercial.interfaces.rest.transform;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.commercial.domain.model.commands.SubmitCommercialInquiryCommand;

import com.molinex.platform.commercial.interfaces.rest.resources.SubmitCommercialInquiryResource;

public final class SubmitCommercialInquiryCommandFromResourceAssembler {
    private SubmitCommercialInquiryCommandFromResourceAssembler() {
    }

    public static SubmitCommercialInquiryCommand toCommandFromResource(SubmitCommercialInquiryResource resource) {
        return new SubmitCommercialInquiryCommand(resource.fullName(), resource.email(), resource.phoneNumber(),
                resource.companyName(), resource.message());
    }
}
