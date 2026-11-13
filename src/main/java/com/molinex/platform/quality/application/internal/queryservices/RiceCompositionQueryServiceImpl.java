package com.molinex.platform.quality.application.internal.queryservices;

import com.molinex.platform.quality.application.internal.outboundservices.acl.ExternalProductionService;
import com.molinex.platform.quality.application.queryservices.RiceCompositionQueryService;
import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.queries.GetRiceCompositionQuery;
import com.molinex.platform.quality.domain.repositories.QualityAssessmentRepository;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiceCompositionQueryServiceImpl implements RiceCompositionQueryService {
    private final QualityAssessmentRepository repository;
    private final ExternalProductionService production;

    public Result<List<QualityAssessment>, ApplicationError> handle(GetRiceCompositionQuery query) {
        if (query.batchId() == null || query.batchId() <= 0)
            return Result.failure(ApplicationError.validationError("batchId", "Must be positive"));
        if (!production.batchExists(query.batchId()))
            return Result.failure(ApplicationError.notFound("ProductionBatch", query.batchId().toString()));
        var records = production.recordIds(query.batchId());
        return Result.success(
                repository.findAll().stream().filter(a -> records.contains(a.getProductionRecordId().value()))
                        .sorted(Comparator.comparing(QualityAssessment::getAssessedAt)).toList());
    }
}
