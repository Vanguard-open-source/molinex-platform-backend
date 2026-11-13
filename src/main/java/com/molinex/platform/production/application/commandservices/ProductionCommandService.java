package com.molinex.platform.production.application.commandservices;

import com.molinex.platform.production.domain.model.commands.RegisterProductionBatchCommand;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

public interface ProductionCommandService {
    Result<ProductionBatchId, ApplicationError> handle(RegisterProductionBatchCommand command);
}
