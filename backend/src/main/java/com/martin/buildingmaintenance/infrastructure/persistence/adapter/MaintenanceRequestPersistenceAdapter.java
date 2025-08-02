package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAMaintenanceRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MaintenanceRequestPersistenceAdapter implements MaintenanceRequestRepository {

    private final JPAMaintenanceRequestRepository jpa;
    private final MaintenanceRequestMapper mapper;

    @Override
    public List<MaintenanceRequest> findByResidentId(UUID residentId) {
        return jpa.findByResident_Id(residentId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<MaintenanceRequest> findByIdAndResidentId(UUID requestId, UUID residentId) {
        return jpa.findByIdAndResident_Id(requestId, residentId).map(mapper::toDomain);
    }

    @Override
    public List<MaintenanceRequest> findByAssignedTechnicianId(UUID technicianId) {
        return jpa.findByTechnicianId(technicianId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<MaintenanceRequest> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<MaintenanceRequest> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public MaintenanceRequest save(MaintenanceRequest request) {
        var entity = mapper.toEntity(request);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
