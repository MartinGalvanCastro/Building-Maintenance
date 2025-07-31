package com.martin.buildingmaintenance.application.port.out;

import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.domain.model.RequestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaintenanceRequestRepository {
    // Resident’s requests
    List<MaintenanceRequest> findByResidentId(UUID residentId);
    Optional<MaintenanceRequest> findByIdAndResidentId(UUID requestId, UUID residentId);

    // Technician’s assignments
    List<MaintenanceRequest> findByAssignedTechnicianId(UUID technicianId);

    // CRUD
    List<MaintenanceRequest> findAll();
    Optional<MaintenanceRequest> findById(UUID id);
    MaintenanceRequest save(MaintenanceRequest request);
    void deleteById(UUID id);
}