package com.molinex.platform.quality.application.queryservices;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.queries.GetAllQualityResultsQuery;
import com.molinex.platform.quality.domain.model.queries.GetQualityAssessmentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface QualityAssessmentQueryService {
    List<QualityAssessment> handle(GetAllQualityResultsQuery query);

    Optional<QualityAssessment> handle(GetQualityAssessmentByIdQuery query);
}
