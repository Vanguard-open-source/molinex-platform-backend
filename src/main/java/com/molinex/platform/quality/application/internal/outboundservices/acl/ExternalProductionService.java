package com.molinex.platform.quality.application.internal.outboundservices.acl;

import com.molinex.platform.production.interfaces.acl.ProductionContextFacade;
import com.molinex.platform.production.interfaces.acl.ProductionRecordSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalProductionService {
    private final ProductionContextFacade facade;

    public boolean recordExists(Integer id) {
        return facade.findRecord(id).isPresent();
    }

    public boolean batchExists(Integer id) {
        return facade.batchExists(id);
    }

    public List<Integer> recordIds(Integer batchId) {
        return facade.findRecords(batchId).stream().map(ProductionRecordSnapshot::id).toList();
    }
}
