package com.martin.buildingmaintenance.application.service;

import com.martin.buildingmaintenance.application.dto.CancelResponseDto;
import com.martin.buildingmaintenance.application.dto.CreateRequestDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.UpdateRequestDto;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.application.port.out.ResidentRepository;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.domain.model.RequestStatus;
import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentRequestServiceImplTest {
    @Mock
    private MaintenanceRequestRepository requestRepo;
    @Mock
    private ResidentRepository residentRepo;
    @Mock
    private MaintenanceRequestMapper mapper;
    @InjectMocks
    private ResidentRequestServiceImpl service;

    private final UUID residentId = UUID.randomUUID();
    private final UUID requestId = UUID.randomUUID();

    @Test
    void listMyRequests_returnsMappedList() {
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findByResidentId(residentId)).thenReturn(List.of(req));
        when(mapper.toDto(req)).thenReturn(dto);
        List<MaintenanceRequestDto> result = service.listMyRequests(residentId);
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getMyRequest_returnsMappedDto() {
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.of(req));
        when(mapper.toDto(req)).thenReturn(dto);
        MaintenanceRequestDto result = service.getMyRequest(residentId, requestId);
        assertSame(dto, result);
    }

    @Test
    void getMyRequest_notFound_throws() {
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getMyRequest(residentId, requestId));
    }

    @Test
    void createRequest_success() {
        CreateRequestDto dto = mock(CreateRequestDto.class);
        Resident resident = mock(Resident.class);
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        MaintenanceRequest saved = mock(MaintenanceRequest.class);
        MaintenanceRequestDto outDto = mock(MaintenanceRequestDto.class);
        when(residentRepo.findById(residentId)).thenReturn(Optional.of(resident));
        when(requestRepo.save(any())).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(outDto);
        when(dto.description()).thenReturn("desc");
        when(dto.specialization()).thenReturn(null);
        when(dto.scheduledAt()).thenReturn(LocalDateTime.now().plusDays(1));
        MaintenanceRequestDto result = service.createRequest(residentId, dto);
        assertSame(outDto, result);
    }

    @Test
    void createRequest_residentNotFound_throws() {
        CreateRequestDto dto = mock(CreateRequestDto.class);
        when(residentRepo.findById(residentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.createRequest(residentId, dto));
    }

    @Test
    void updateMyRequest_success() {
        UpdateRequestDto dto = mock(UpdateRequestDto.class);
        MaintenanceRequest existing = MaintenanceRequest.builder()
                .id(requestId)
                .description("old desc")
                .scheduledAt(LocalDateTime.now())
                .build();
        MaintenanceRequest saved = mock(MaintenanceRequest.class);
        MaintenanceRequestDto outDto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.of(existing));
        when(dto.description()).thenReturn("desc");
        when(dto.scheduledAt()).thenReturn(LocalDateTime.now().plusDays(1));
        when(requestRepo.save(any())).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(outDto);
        MaintenanceRequestDto result = service.updateMyRequest(residentId, requestId, dto);
        assertSame(outDto, result);
    }

    @Test
    void updateMyRequest_notFound_throws() {
        UpdateRequestDto dto = mock(UpdateRequestDto.class);
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateMyRequest(residentId, requestId, dto));
    }

    @Test
    void cancelMyRequest_success() {
        MaintenanceRequest existing = MaintenanceRequest.builder()
                .id(requestId)
                .description("desc")
                .scheduledAt(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.of(existing));
        CancelResponseDto result = service.cancelMyRequest(residentId, requestId);
        assertEquals("Request cancelled successfully", result.message());
        verify(requestRepo).save(existing);
    }

    @Test
    void cancelMyRequest_notFound_throws() {
        when(requestRepo.findByIdAndResidentId(requestId, residentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.cancelMyRequest(residentId, requestId));
    }
}
