package com.molinex.platform.quality.application.commandservices;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.commands.RecordProductionWasteCommand;
import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

public interface WasteRecordCommandService {
    Result<WasteRecord, ApplicationError> handle(RecordProductionWasteCommand command);
}
