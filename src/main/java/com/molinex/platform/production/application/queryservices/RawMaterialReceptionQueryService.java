package com.molinex.platform.production.application.queryservices;

import com.molinex.platform.production.domain.model.aggregates.RawMaterialReception;
import com.molinex.platform.production.domain.model.valueobjects.RawMaterialReceptionId;
import com.molinex.platform.production.domain.repositories.RawMaterialReceptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RawMaterialReceptionQueryService {

    private final RawMaterialReceptionRepository repository;

    public RawMaterialReceptionQueryService(RawMaterialReceptionRepository repository) {
        this.repository = repository;
    }

    public List<RawMaterialReception> findAll() {
        return repository.findAll();
    }

    public Optional<RawMaterialReception> findById(long id) {
        return repository.findById(new RawMaterialReceptionId(id));
    }
}
