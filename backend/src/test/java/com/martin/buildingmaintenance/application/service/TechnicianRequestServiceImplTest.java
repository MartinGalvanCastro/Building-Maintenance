package com.martin.buildingmaintenance.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.application.dto.ChangeStatusDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.exception.AccessDeniedException;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.domain.model.RequestStatus;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TechnicianRequestServiceImplTest {
    @Mock MaintenanceRequestRepository repo;
    @Mock MaintenanceRequestMapper mapper;
    @InjectMocks TechnicianRequestServiceImpl service;

    @Test
    void listAssignments_returnsMappedList() {
        UUID techId = UUID.randomUUID();
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        when(repo.findByAssignedTechnicianId(techId)).thenReturn(List.of(req));
        var result = service.listAssignments(techId);
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    void changeStatus_success() {
        UUID techId = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        Technician tech = Technician.builder().id(techId).build();
        MaintenanceRequest existing =
                MaintenanceRequest.builder()
                        .id(reqId)
                        .technician(tech)
                        .status(RequestStatus.PENDING)
                        .build();
        ChangeStatusDto dto = mock(ChangeStatusDto.class);
        when(dto.status()).thenReturn(RequestStatus.COMPLETED);
        MaintenanceRequest saved = mock(MaintenanceRequest.class);
        MaintenanceRequestDto outDto = mock(MaintenanceRequestDto.class);
        when(repo.findById(reqId)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(outDto);
        MaintenanceRequestDto result = service.changeStatus(techId, reqId, dto);
        assertSame(outDto, result);
    }

    @Test
    void changeStatus_requestNotFound_throws() {
        UUID techId = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        ChangeStatusDto dto = mock(ChangeStatusDto.class);
        when(repo.findById(reqId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.changeStatus(techId, reqId, dto));
    }

    @Test
    void changeStatus_notAssigned_throws() {
        UUID techId = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        Technician otherTech = Technician.builder().id(UUID.randomUUID()).build();
        MaintenanceRequest existing =
                MaintenanceRequest.builder()
                        .id(reqId)
                        .technician(otherTech)
                        .status(RequestStatus.PENDING)
                        .build();
        ChangeStatusDto dto = mock(ChangeStatusDto.class);
        when(repo.findById(reqId)).thenReturn(Optional.of(existing));
        assertThrows(AccessDeniedException.class, () -> service.changeStatus(techId, reqId, dto));
    }

    @Test
    void changeStatus_noTechnicianAssigned_throws() {
        UUID techId = UUID.randomUUID();
        UUID reqId = UUID.randomUUID();
        MaintenanceRequest existing =
                MaintenanceRequest.builder().id(reqId).status(RequestStatus.PENDING).build();
        ChangeStatusDto dto = mock(ChangeStatusDto.class);
        when(repo.findById(reqId)).thenReturn(Optional.of(existing));
        assertThrows(AccessDeniedException.class, () -> service.changeStatus(techId, reqId, dto));
    }
}
