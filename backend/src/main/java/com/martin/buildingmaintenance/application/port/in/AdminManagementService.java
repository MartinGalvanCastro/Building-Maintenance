package com.martin.buildingmaintenance.application.port.in;

import com.martin.buildingmaintenance.application.dto.*;

import java.util.List;
import java.util.UUID;

public interface AdminManagementService {
    List<ResidentialComplexDto> listAllComplexes();
    ResidentialComplexDto       getComplex(UUID complexId);
    ResidentialComplexDto       saveComplex(ResidentialComplexCommandDto dto);
    ResidentialComplexDto       updateComplex(UUID complexId, ResidentialComplexCommandDto dto);
    DeleteResponseDto                        deleteComplex(UUID complexId);

    // Residents
    List<ResidentDto> listAllResidents();
    ResidentDto                 getResident(UUID residentId);
    ResidentDto saveResident(ResidentCreateCommandDto dto);
    ResidentDto                 updateResident(UUID residentId, ResidentUpdateCommandDto dto);
    DeleteResponseDto deleteResident(UUID residentId);

    // Technicians
    List<TechnicianDto>         listAllTechnicians();
    TechnicianDto               getTechnician(UUID technicianId);
    TechnicianDto               saveTechnician(TechnicianCreateCommandDto dto);
    TechnicianDto               updateTechnician(UUID technicianId, TechnicianUpdateCommandDto dto);
    DeleteResponseDto                        deleteTechnician(UUID technicianId);

    // Maintenance Requests
    List<MaintenanceRequestDto> listAllRequests();
    MaintenanceRequestDto       getRequest(UUID requestId);
    MaintenanceRequestDto       assignTechnician(UUID requestId, UUID technicianId);
    DeleteResponseDto                        deleteRequest(UUID requestId);
}
