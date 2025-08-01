package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.MaintenanceRequestEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAMaintenanceRequestRepository
        extends JpaRepository<MaintenanceRequestEntity, UUID> {
    // Resident: list my requests
    List<MaintenanceRequestEntity> findByResident_Id(UUID residentId);

    // Resident: get one of my requests
    Optional<MaintenanceRequestEntity> findByIdAndResident_Id(UUID requestId, UUID residentId);

    // Technician: list requests assigned to me
    List<MaintenanceRequestEntity> findByTechnicianId(UUID technicianId);
}
