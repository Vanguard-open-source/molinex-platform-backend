package com.molinex.platform.production.application.queryservices;

import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.queries.GetProductionHistoryQuery;
import com.molinex.platform.production.domain.model.queries.GetProductionProcessesQuery;
import com.molinex.platform.production.domain.model.valueobjects.ProductionBatchId;
import com.molinex.platform.production.domain.model.valueobjects.ProductionRecordId;
import com.molinex.platform.production.domain.repositories.ProductionRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionRecordQueryService {

    private final ProductionRecordRepository records;

    public ProductionRecordQueryService(ProductionRecordRepository records) {
        this.records = records;
    }

    public List<ProductionRecord> handle(GetProductionProcessesQuery query) {
        var batchId = query.batchId() == null ? null : new ProductionBatchId(query.batchId());
        return records.findProcesses(batchId);
    }

    public List<ProductionRecord> handle(GetProductionHistoryQuery query) {
        return records.findHistory(query.from(), query.to());
    }

    public Optional<ProductionRecord> findById(long id) {
        return records.findById(new ProductionRecordId(id));
    }
}
