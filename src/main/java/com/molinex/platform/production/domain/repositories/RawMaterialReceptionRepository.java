package com.molinex.platform.production.domain.repositories;

import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;

public interface RawMaterialReceptionRepository {
    boolean existsById(RawMaterialReceptionId id);
}
