package com.molinex.platform.quality.application.internal.outboundservices.acl;

import com.molinex.platform.production.interfaces.acl.ProductionContextFacade;
import com.molinex.platform.production.interfaces.acl.ProductionRecordSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import com.molinex.platform.quality.domain.model.valueobjects.ProductionRecordId;

@Service
@RequiredArgsConstructor
public class ExternalProductionService {
    private final ProductionContextFacade facade;

    public boolean recordExists(ProductionRecordId id) {
        return facade.findRecord(id.value()).isPresent();
    }

    public boolean batchExists(Long id) {
        return facade.batchExists(id);
    }

    public Set<ProductionRecordId> recordIds(Long batchId) {
        return facade.findRecords(batchId).stream().map(ProductionRecordSnapshot::id).map(ProductionRecordId::new).collect(Collectors.toUnmodifiableSet());
    }
}
