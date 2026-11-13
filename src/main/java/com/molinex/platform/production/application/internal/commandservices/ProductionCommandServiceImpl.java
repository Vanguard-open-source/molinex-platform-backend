package com.molinex.platform.production.application.internal.commandservices;

import com.molinex.platform.production.application.commandservices.ProductionCommandService;
import com.molinex.platform.production.domain.model.aggregates.ProductionBatch;
import com.molinex.platform.production.domain.model.commands.RegisterProductionBatchCommand;
import com.molinex.platform.production.domain.model.valueobjects.BatchCode;
import com.molinex.platform.production.domain.model.valueobjects.BatchRegistrationDateTime;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import com.molinex.platform.shared.application.result.Result;

public final class ProductionCommandServiceImpl implements ProductionCommandService {

    private final RawMaterialReceptionRepository receptionRepository;
    private final ProductionBatchRepository batchRepository;

    public ProductionCommandServiceImpl(RawMaterialReceptionRepository receptionRepository,
                                        ProductionBatchRepository batchRepository) {
        this.receptionRepository = receptionRepository;
        this.batchRepository = batchRepository;
    }

    @Override
    public Result<ProductionBatchId, ApplicationError> handle(RegisterProductionBatchCommand command) {
        if (command.code() == null || command.code().isBlank()) {
            return validationFailure("code", "Batch code is required");
        }
        if (command.receptionId() == null || command.receptionId() <= 0) {
            return validationFailure("receptionId", "Reception ID must be positive");
        }
        if (command.registeredAt() == null) {
            return validationFailure("registeredAt", "Registration date is required");
        }

        var receptionId = new RawMaterialReceptionId(command.receptionId());
        if (!receptionRepository.existsById(receptionId)) {
            return Result.failure(new ApplicationError(
                    "reception-not-found", "Raw material reception does not exist", ErrorType.NOT_FOUND));
        }

        var code = new BatchCode(command.code());
        if (batchRepository.existsByCode(code)) {
            return Result.failure(new ApplicationError(
                    "batch-code-conflict", "Batch code already exists", ErrorType.CONFLICT));
        }

        var batch = ProductionBatch.register(
                code, receptionId, new BatchRegistrationDateTime(command.registeredAt()));
        return Result.success(batchRepository.save(batch).id());
    }

    private Result<ProductionBatchId, ApplicationError> validationFailure(String field, String message) {
        return Result.failure(new ApplicationError(field, message, ErrorType.VALIDATION));
    }
}
