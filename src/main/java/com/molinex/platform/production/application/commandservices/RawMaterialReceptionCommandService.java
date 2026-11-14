
package com.molinex.platform.production.application.commandservices;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.commands.RecordRawMaterialReceptionCommand;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

public interface RawMaterialReceptionCommandService {

    Result<RawMaterialReception, ApplicationError> handle(
            RecordRawMaterialReceptionCommand command
    );
}
