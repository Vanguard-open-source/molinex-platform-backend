package com.molinex.platform.production.infrastructure.persistence.jpa.adapters;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import com.molinex.platform.production.infrastructure.persistence.jpa.assemblers.RawMaterialReceptionPersistenceAssembler;
import com.molinex.platform.production.infrastructure.persistence.jpa.repositories.RawMaterialReceptionPersistenceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RawMaterialReceptionRepositoryImpl implements RawMaterialReceptionRepository {

    private final RawMaterialReceptionPersistenceRepository persistence;

    public RawMaterialReceptionRepositoryImpl(RawMaterialReceptionPersistenceRepository persistence) {
        this.persistence = persistence;
    }

    @Override
    public boolean existsById(RawMaterialReceptionId id) {
        return persistence.existsById(id.value());
    }

    @Override
    public RawMaterialReception save(RawMaterialReception reception) {
        var saved = persistence.saveAndFlush(RawMaterialReceptionPersistenceAssembler.toEntity(reception));
        return RawMaterialReceptionPersistenceAssembler.toDomain(saved);
    }

    @Override
    public List<RawMaterialReception> findAll() {
        return persistence.findAll(Sort.by("id")).stream()
                .map(RawMaterialReceptionPersistenceAssembler::toDomain).toList();
    }

    @Override
    public Optional<RawMaterialReception> findById(RawMaterialReceptionId id) {
        return persistence.findById(id.value()).map(RawMaterialReceptionPersistenceAssembler::toDomain);
    }
}
