package com.molinex.platform.quality.domain.repositories;

import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;

import java.util.List;
import java.util.Optional;

public interface WasteRecordRepository {
    WasteRecord save(WasteRecord aggregate);

    Optional<WasteRecord> findById(WasteRecordId id);

    List<WasteRecord> findAll();
}
