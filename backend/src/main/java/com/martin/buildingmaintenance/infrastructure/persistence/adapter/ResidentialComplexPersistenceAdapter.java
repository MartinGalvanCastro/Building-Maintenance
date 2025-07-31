package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.ResidentialComplexRepository;
import com.martin.buildingmaintenance.domain.model.ResidentialComplex;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentialComplexMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentialComplexEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAResidentialComplexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResidentialComplexPersistenceAdapter implements ResidentialComplexRepository {
    private final JPAResidentialComplexRepository jpa;
    private final ResidentialComplexMapper       mapper;

    @Override
    public List<ResidentialComplex> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ResidentialComplex> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public ResidentialComplex save(ResidentialComplex complex) {
        var entity = mapper.toEntity(complex);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
