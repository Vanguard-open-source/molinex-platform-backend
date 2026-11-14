package com.molinex.platform.production.domain.repositories;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;

import java.util.List;
import java.util.Optional;

public interface RawMaterialReceptionRepository {

    boolean existsById(RawMaterialReceptionId id);

    RawMaterialReception save(RawMaterialReception reception);

    List<RawMaterialReception> findAll();

    Optional<RawMaterialReception> findById(RawMaterialReceptionId id);
}
