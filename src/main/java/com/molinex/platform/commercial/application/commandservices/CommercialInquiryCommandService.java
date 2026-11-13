package com.molinex.platform.commercial.application.commandservices;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.molinex.platform.commercial.domain.model.commands.SubmitCommercialInquiryCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;

public interface CommercialInquiryCommandService {
    Result<CommercialInquiry, ApplicationError> handle(SubmitCommercialInquiryCommand command);
}
