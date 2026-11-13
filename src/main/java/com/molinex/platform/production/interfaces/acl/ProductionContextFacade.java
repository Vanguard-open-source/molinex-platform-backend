package com.molinex.platform.production.interfaces.acl;

import java.util.List;
import java.util.Optional;

public interface ProductionContextFacade {

    Optional<ProductionRecordSnapshot> findRecord(Long id);

    List<ProductionRecordSnapshot> findRecords(Long batchId);

    boolean batchExists(Long batchId);
}
