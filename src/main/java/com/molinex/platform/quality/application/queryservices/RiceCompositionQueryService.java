package com.molinex.platform.quality.application.queryservices;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.queries.GetRiceCompositionQuery;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

import java.util.List;

public interface RiceCompositionQueryService {
    Result<List<QualityAssessment>, ApplicationError> handle(GetRiceCompositionQuery query);
}
