package com.molinex.platform.production.application.internal.commandservices;

import com.molinex.platform.production.application.commandservices.ProductionCommandService;
import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.commands.RegisterProductionBatchCommand;

import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;

@Service
public class ProductionBatchRegistrationService {

    private final ProductionCommandService commands;
    private final ProductionBatchRepository batches;
    private final ApplicationEventPublisher events;
    private final TransactionTemplate transactions;

    public ProductionBatchRegistrationService(RawMaterialReceptionRepository receptions,
                                              ProductionBatchRepository repository,
                                              ApplicationEventPublisher events,
                                              TransactionTemplate transactions) {
        this.commands = new ProductionCommandServiceImpl(receptions, repository);
        this.batches = repository;
        this.events = events;
        this.transactions = transactions;
    }

    public Result<ProductionBatch, ApplicationError> register(RegisterProductionBatchCommand command) {
        if (command == null) {
            return Result.failure(new ApplicationError("invalid-batch", "Registration command is required", ErrorType.VALIDATION));
        }
        if (command.code() != null && command.code().length() > 50) {
            return Result.failure(new ApplicationError("code", "Batch code must have at most 50 characters", ErrorType.VALIDATION));
        }

        try {
            return transactions.execute(transaction -> {
                var result = commands.handle(command);
                if (!result.isSuccess()) {
                    return Result.failure(result.error());
                }
                var batch = batches.findById(result.value()).orElseThrow();
                events.publishEvent(batch.registrationEvent(Instant.now()));
                return Result.success(batch);
            });
        } catch (DataIntegrityViolationException exception) {
            return Result.failure(new ApplicationError(
                    "batch-data-conflict", "Batch data conflicts with an existing record", ErrorType.CONFLICT));
        }
    }
}
