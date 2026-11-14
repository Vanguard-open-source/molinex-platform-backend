package com.molinex.platform.production.application.internal.commandservices;

import com.molinex.platform.production.application.commandservices.ProductionRecordCommandService;
import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.commands.RecordProductionInformationCommand;
import com.molinex.platform.production.domain.model.commands.UpdateProductionInformationCommand;
import com.molinex.platform.production.domain.model.events.ProductionInformationRecorded;
import com.molinex.platform.production.domain.model.events.ProductionInformationUpdated;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionDetails;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.production.domain.repositories.ProductionBatchRepository;
import com.molinex.platform.production.domain.repositories.ProductionRecordRepository;
import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductionRecordCommandServiceImpl implements ProductionRecordCommandService {

    private final ProductionRecordRepository records;
    private final ProductionBatchRepository batches;
    private final ApplicationEventPublisher events;

    public ProductionRecordCommandServiceImpl(ProductionRecordRepository records, ProductionBatchRepository batches,
                                              ApplicationEventPublisher events) {
        this.records = records;
        this.batches = batches;
        this.events = events;
    }

    @Override
    @Transactional
    public Result<ProductionRecord, ApplicationError> handle(RecordProductionInformationCommand command) {
        if (command == null) {
            return invalid("Production command is required");
        }
        ProductionRecord record;
        try {
            var details = new ProductionDetails(command.processName(),
                    new Weight(command.processedWeightValue(), command.processedWeightUnit()),
                    command.startedAt(), command.finishedAt(), command.status());
            record = new ProductionRecord(null, new ProductionBatchId(command.batchId()), details, command.recordedAt());
        } catch (IllegalArgumentException exception) {
            return invalid(exception.getMessage());
        }

        if (batches.findById(record.batchId()).isEmpty()) {
            return Result.failure(new ApplicationError("batch-not-found", "Production batch does not exist", ErrorType.NOT_FOUND));
        }

        var saved = records.save(record);
        events.publishEvent(new ProductionInformationRecorded(saved.id().value()));
        return Result.success(saved);
    }

    @Override
    @Transactional
    public Result<ProductionRecord, ApplicationError> handle(UpdateProductionInformationCommand command) {
        if (command == null) {
            return invalid("Update command is required");
        }
        ProductionRecordId recordId;
        try {
            recordId = new ProductionRecordId(command.id());
        } catch (IllegalArgumentException exception) {
            return invalid(exception.getMessage());
        }
        var existing = records.findById(recordId);
        if (existing.isEmpty()) {
            return Result.failure(new ApplicationError(
                    "production-record-not-found", "Production record does not exist", ErrorType.NOT_FOUND));
        }

        var record = existing.get();
        try {
            var details = new ProductionDetails(command.processName(),
                    new Weight(command.processedWeightValue(), command.processedWeightUnit()),
                    command.startedAt(), command.finishedAt(), command.status());
            record.updateDetails(new ProductionBatchId(command.batchId()), details);
        } catch (IllegalArgumentException exception) {
            return invalid(exception.getMessage());
        }

        var saved = records.save(record);
        events.publishEvent(new ProductionInformationUpdated(saved.id().value()));
        return Result.success(saved);
    }

    private Result<ProductionRecord, ApplicationError> invalid(String message) {
        return Result.failure(new ApplicationError("invalid-production-record", message, ErrorType.VALIDATION));
    }
}
