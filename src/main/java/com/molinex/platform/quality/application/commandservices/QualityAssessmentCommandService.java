package com.molinex.platform.quality.application.commandservices;

import com.molinex.platform.quality.domain.model.aggregates.QualityAssessment;
import com.molinex.platform.quality.domain.model.commands.RecordQualityResultsCommand;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

public interface QualityAssessmentCommandService {
    Result<QualityAssessment, ApplicationError> handle(RecordQualityResultsCommand command);
}
