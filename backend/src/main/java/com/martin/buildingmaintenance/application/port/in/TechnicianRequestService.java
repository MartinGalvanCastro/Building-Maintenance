package com.martin.buildingmaintenance.application.port.in;

import com.martin.buildingmaintenance.application.dto.ChangeStatusDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;

import java.util.List;
import java.util.UUID;

public interface TechnicianRequestService {
    List<MaintenanceRequestDto> listAssignments(UUID technicianId);

    MaintenanceRequestDto changeStatus(
            UUID technicianId,
            UUID requestId,
            ChangeStatusDto dto
    );

}
