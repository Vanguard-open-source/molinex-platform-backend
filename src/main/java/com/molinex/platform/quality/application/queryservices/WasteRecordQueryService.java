package com.molinex.platform.quality.application.queryservices;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.queries.GetProductionWasteQuery;
import com.molinex.platform.quality.domain.model.queries.GetWasteRecordByIdQuery;

import java.util.List;
import java.util.Optional;

public interface WasteRecordQueryService {
    Optional<WasteRecord> handle(GetWasteRecordByIdQuery query);

    List<WasteRecord> handle(GetProductionWasteQuery query);
}
