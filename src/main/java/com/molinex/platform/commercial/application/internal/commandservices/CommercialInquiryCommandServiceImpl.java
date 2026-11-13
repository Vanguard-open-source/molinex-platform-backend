package com.molinex.platform.commercial.application.internal.commandservices;

import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.molinex.platform.commercial.domain.model.valueobjects.ContactInformation;
import com.molinex.platform.commercial.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.commercial.domain.model.valueobjects.InquiryMessage;
import com.molinex.platform.commercial.domain.model.valueobjects.PhoneNumber;
import com.molinex.platform.commercial.domain.model.commands.SubmitCommercialInquiryCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import com.molinex.platform.commercial.application.commandservices.CommercialInquiryCommandService;
import com.molinex.platform.commercial.domain.model.aggregates.CommercialInquiry;
import com.molinex.platform.commercial.domain.repositories.CommercialInquiryRepository;

@Service
@RequiredArgsConstructor
public class CommercialInquiryCommandServiceImpl implements CommercialInquiryCommandService {
    private final CommercialInquiryRepository repository;

    @Override
    public Result<CommercialInquiry, ApplicationError> handle(SubmitCommercialInquiryCommand command) {
        try {

            var aggregate = new CommercialInquiry(null,
                    new ContactInformation(command.fullName(), new EmailAddress(command.email()),
                            command.phoneNumber() == null ? null : new PhoneNumber(command.phoneNumber()),
                            command.companyName()), new InquiryMessage(command.message()), Instant.now());
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("CommercialInquiry", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("CommercialInquiry", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(
                    ApplicationError.unexpected("CommercialInquiry", "The operation could not be confirmed"));
        }
    }
}
