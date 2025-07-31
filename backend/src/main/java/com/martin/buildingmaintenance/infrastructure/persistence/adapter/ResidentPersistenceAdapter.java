package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.ResidentRepository;
import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResidentPersistenceAdapter implements ResidentRepository {
    private final JPAResidentRepository jpa;
    private final ResidentMapper        mapper;

    @Override
    public List<Resident> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Resident> findById(UUID id) {
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Resident save(Resident resident) {
        var entity = mapper.toEntity(resident);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
