package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.TechnicianRepository;
import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.infrastructure.mapper.TechnicianMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPATechnicianRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TechnicianPersistenceAdapter implements TechnicianRepository {

    private final JPATechnicianRepository jpa;
    private final TechnicianMapper mapper;

    @Override
    public List<Technician> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Technician> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Technician> findBySpecialization(Specialization specialization) {
        return jpa.findBySpecializationsContaining(specialization).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Technician save(Technician technician) {
        var entity = mapper.toEntity(technician);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
