package com.molinex.platform.quality.application.internal.queryservices;

import com.molinex.platform.quality.application.queryservices.WasteRecordQueryService;
import com.molinex.platform.quality.domain.model.aggregates.WasteRecord;
import com.molinex.platform.quality.domain.model.queries.GetProductionWasteQuery;
import com.molinex.platform.quality.domain.model.queries.GetWasteRecordByIdQuery;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.quality.domain.model.valueobjects.WasteRecordId;
import com.molinex.platform.quality.domain.repositories.WasteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WasteRecordQueryServiceImpl implements WasteRecordQueryService {
    private final WasteRecordRepository repository;

    public Optional<WasteRecord> handle(GetWasteRecordByIdQuery query) {
        return repository.findById(new WasteRecordId(query.id()));
    }

    public List<WasteRecord> handle(GetProductionWasteQuery query) {
        if (query.productionRecordId() != null) new ProductionRecordId(query.productionRecordId());
        return repository.findAll().stream().filter(r -> query.productionRecordId() == null ||
                r.getProductionRecordId().value().equals(query.productionRecordId())).toList();
    }
}
