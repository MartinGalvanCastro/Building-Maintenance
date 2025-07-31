package com.martin.buildingmaintenance.application.service;


import com.martin.buildingmaintenance.application.dto.CancelResponseDto;
import com.martin.buildingmaintenance.application.dto.CreateRequestDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.UpdateRequestDto;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.in.ResidentRequestService;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.application.port.out.ResidentRepository;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.domain.model.RequestStatus;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResidentRequestServiceImpl implements ResidentRequestService {

    private final MaintenanceRequestRepository requestRepo;
    private final ResidentRepository residentRepo;
    private final MaintenanceRequestMapper mapper;

    @Override
    public List<MaintenanceRequestDto> listMyRequests(UUID residentId) {
        log.info("Listing requests for residentId={}", residentId);
        return requestRepo.findByResidentId(residentId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MaintenanceRequestDto getMyRequest(UUID residentId, UUID requestId) {
        log.info("Getting requestId={} for residentId={}", requestId, residentId);
        var request = requestRepo.findByIdAndResidentId(requestId, residentId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        return mapper.toDto(request);
    }

    @Override
    public MaintenanceRequestDto createRequest(UUID residentId, CreateRequestDto dto) {
        log.info("Creating request for residentId={}", residentId);
        var resident = residentRepo.findById(residentId)
                .orElseThrow(() -> new NotFoundException("Resident not found"));
        var newReq = MaintenanceRequest.builder()
                .id(UUID.randomUUID())
                .resident(resident)
                .description(dto.description())
                .specialization(dto.specialization())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .scheduledAt(dto.scheduledAt())
                .build();
        MaintenanceRequest saved = requestRepo.save(newReq);
        log.info("Created request with id={} for residentId={}", saved.getId(), residentId);
        return mapper.toDto(saved);
    }

    @Override
    public MaintenanceRequestDto updateMyRequest(UUID residentId, UUID requestId, UpdateRequestDto dto) {
        log.info("Updating requestId={} for residentId={}", requestId, residentId);
        var existing = requestRepo.findByIdAndResidentId(requestId, residentId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        var updated  = existing.toBuilder().
                description(dto.description())
                .scheduledAt(dto.scheduledAt())
                .build();
        var saved = requestRepo.save(updated);
        log.info("Updated requestId={} for residentId={}", requestId, residentId);
        return mapper.toDto(saved);
    }

    @Override
    public CancelResponseDto cancelMyRequest(UUID residentId, UUID requestId) {
        log.info("Cancelling requestId={} for residentId={}", requestId, residentId);
        var existing = requestRepo.findByIdAndResidentId(requestId, residentId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        existing.toBuilder().status(RequestStatus.CANCELLED).build();
        requestRepo.save(existing);
        log.info("Cancelled requestId={} for residentId={}", requestId, residentId);
        return new CancelResponseDto("Request cancelled successfully");
    }
}
