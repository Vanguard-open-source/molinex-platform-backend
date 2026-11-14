package com.molinex.platform.production.application.acl;

import com.molinex.platform.production.application.queryservices.ProductionBatchQueryService;
import com.molinex.platform.production.application.queryservices.ProductionRecordQueryService;
import com.molinex.platform.production.domain.model.aggregates.ProductionRecord;
import com.molinex.platform.production.domain.model.queries.GetProductionProcessesQuery;
import com.molinex.platform.production.interfaces.acl.ProductionContextFacade;
import com.molinex.platform.production.interfaces.acl.ProductionRecordSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionContextFacadeImpl implements ProductionContextFacade {

    private final ProductionRecordQueryService records;
    private final ProductionBatchQueryService batches;

    public ProductionContextFacadeImpl(ProductionRecordQueryService records, ProductionBatchQueryService batches) {
        this.records = records;
        this.batches = batches;
    }

    @Override
    public Optional<ProductionRecordSnapshot> findRecord(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return records.findById(id).map(ProductionContextFacadeImpl::snapshot);
    }

    @Override
    public List<ProductionRecordSnapshot> findRecords(Long batchId) {
        return records.handle(new GetProductionProcessesQuery(batchId)).stream()
                .map(ProductionContextFacadeImpl::snapshot).toList();
    }

    @Override
    public boolean batchExists(Long batchId) {
        return batchId != null && batchId > 0 && batches.findById(batchId).isPresent();
    }

    private static ProductionRecordSnapshot snapshot(ProductionRecord record) {
        return new ProductionRecordSnapshot(record.id().value(), record.batchId().value(), record.recordedAt());
    }
}
