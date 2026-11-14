package com.molinex.platform.quality.application.internal.queryservices;

import com.molinex.platform.quality.application.queryservices.QualityAssessmentQueryService;
import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.queries.GetAllQualityResultsQuery;
import com.molinex.platform.quality.domain.model.queries.GetQualityAssessmentByIdQuery;
import com.molinex.platform.quality.domain.model.valueobjects.QualityAssessmentId;
import com.molinex.platform.quality.domain.repositories.QualityAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QualityAssessmentQueryServiceImpl implements QualityAssessmentQueryService {
    private final QualityAssessmentRepository repository;

    public List<QualityAssessment> handle(GetAllQualityResultsQuery query) {
        return repository.findAll();
    }

    public Optional<QualityAssessment> handle(GetQualityAssessmentByIdQuery query) {
        return repository.findById(new QualityAssessmentId(query.id()));
    }
}
