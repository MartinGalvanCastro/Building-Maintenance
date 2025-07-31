package com.martin.buildingmaintenance.application.port.in;

import com.martin.buildingmaintenance.application.dto.CancelResponseDto;
import com.martin.buildingmaintenance.application.dto.CreateRequestDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.UpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface ResidentRequestService {

    List<MaintenanceRequestDto> listMyRequests(UUID residentId);
    MaintenanceRequestDto      getMyRequest(UUID residentId, UUID requestId);
    MaintenanceRequestDto      createRequest(UUID residentId, CreateRequestDto dto);
    MaintenanceRequestDto      updateMyRequest(UUID residentId, UUID requestId, UpdateRequestDto dto);
    CancelResponseDto           cancelMyRequest(UUID residentId, UUID requestId);


}
