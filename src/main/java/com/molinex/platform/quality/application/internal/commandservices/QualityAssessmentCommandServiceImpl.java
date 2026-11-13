package com.molinex.platform.quality.application.internal.commandservices;

import com.molinex.platform.quality.application.commandservices.QualityAssessmentCommandService;
import com.molinex.platform.quality.application.internal.outboundservices.acl.ExternalProductionService;
import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.commands.RecordQualityResultsCommand;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.repositories.QualityAssessmentRepository;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QualityAssessmentCommandServiceImpl implements QualityAssessmentCommandService {
    private final QualityAssessmentRepository repository;
    private final ExternalProductionService production;

    @Override
    public Result<QualityAssessment, ApplicationError> handle(RecordQualityResultsCommand command) {
        try {
            var id = new ProductionRecordId(command.productionRecordId());
            if (!production.recordExists(id.value()))
                return Result.failure(ApplicationError.notFound("ProductionRecord", id.value().toString()));
            var aggregate = new QualityAssessment(null, new ProductionRecordId(command.productionRecordId()),
                    command.measurements(), command.assessedAt());
            return Result.success(repository.save(aggregate));
        } catch (IllegalArgumentException ex) {
            return Result.failure(ApplicationError.validationError("QualityAssessment", ex.getMessage()));
        } catch (DataIntegrityViolationException ex) {
            return Result.failure(ApplicationError.conflict("QualityAssessment", "Conflicting stored data"));
        } catch (RuntimeException ex) {
            return Result.failure(
                    ApplicationError.unexpected("QualityAssessment", "The operation could not be confirmed"));
        }
    }
}
