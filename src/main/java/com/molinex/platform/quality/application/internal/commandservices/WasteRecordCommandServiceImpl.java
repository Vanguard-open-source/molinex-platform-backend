package com.molinex.platform.quality.application.internal.commandservices;

import com.molinex.platform.productionqualitysharedkernel.domain.model.valueobjects.Weight;
import com.molinex.platform.quality.application.commandservices.WasteRecordCommandService;
import com.molinex.platform.quality.application.internal.outboundservices.acl.ExternalProductionService;
import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.commands.RecordProductionWasteCommand;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.repositories.WasteRecordRepository;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WasteRecordCommandServiceImpl implements WasteRecordCommandService {
    private final WasteRecordRepository repository;
    private final ExternalProductionService production;

    @Override
    public Result<WasteRecord, ApplicationError> handle(RecordProductionWasteCommand command) {
        try {
            var id = new ProductionRecordId(command.productionRecordId());
            if ((command.baseWeightValue() == null) != (command.baseWeightUnit() == null)) return Result.failure(
                    ApplicationError.validationError("baseWeight",
                            "Value and unit must both be supplied or both omitted"));
            if (!production.recordExists(id))
                return Result.failure(ApplicationError.notFound("ProductionRecord", id.value().toString()));
            var aggregate = new WasteRecord(null, id,
                    new Weight(command.quantityValue(), command.quantityUnit()),
                    command.baseWeightValue() == null ? null :
                            new Weight(command.baseWeightValue(), command.baseWeightUnit()), null,
                    command.recordedAt());
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("WasteRecord", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("WasteRecord", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(ApplicationError.unexpected("WasteRecord", "The operation could not be confirmed"));
        }
    }
}
