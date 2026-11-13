package com.molinex.platform.production.application.internal.commandservices;

import com.molinex.platform.production.application.commandservices.RawMaterialReceptionCommandService;
import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.commands.RecordRawMaterialReceptionCommand;
import com.molinex.platform.production.domain.model.events.RawMaterialReceptionRecorded;
import com.molinex.platform.production.domain.model.valueobjects.MaterialOrigin;
import com.molinex.platform.production.domain.model.valueobjects.Supplier;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RawMaterialReceptionCommandServiceImpl implements RawMaterialReceptionCommandService {

    private final RawMaterialReceptionRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public RawMaterialReceptionCommandServiceImpl(RawMaterialReceptionRepository repository,
                                                  ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Result<RawMaterialReception, ApplicationError> handle(RecordRawMaterialReceptionCommand command) {
        if (command == null) {
            return invalidReception("Reception command is required");
        }

        RawMaterialReception reception;
        try {
            reception = new RawMaterialReception(null, command.receivedAt(), new Supplier(command.supplierName()),
                    new MaterialOrigin(command.originDescription()), new Weight(command.quantityValue(), command.quantityUnit()));
        } catch (IllegalArgumentException exception) {
            return invalidReception(exception.getMessage());
        }

        var savedReception = repository.save(reception);
        eventPublisher.publishEvent(new RawMaterialReceptionRecorded(savedReception.getId().value()));
        return Result.success(savedReception);
    }

    private Result<RawMaterialReception, ApplicationError> invalidReception(String message) {
        return Result.failure(new ApplicationError("invalid-reception", message, ErrorType.VALIDATION));
    }
}
