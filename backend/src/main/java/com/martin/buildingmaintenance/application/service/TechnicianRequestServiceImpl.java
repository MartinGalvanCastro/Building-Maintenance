package com.martin.buildingmaintenance.application.service;

import com.martin.buildingmaintenance.application.dto.ChangeStatusDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.in.TechnicianRequestService;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.martin.buildingmaintenance.application.exception.AccessDeniedException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechnicianRequestServiceImpl implements TechnicianRequestService {
    private final MaintenanceRequestRepository repo;
    private final MaintenanceRequestMapper mapper;

    @Override
    public List<MaintenanceRequestDto> listAssignments(UUID technicianId) {
        log.info("Listing assignments for technicianId={}", technicianId);
        return repo.findByAssignedTechnicianId(technicianId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MaintenanceRequestDto changeStatus(UUID technicianId,
                                              UUID requestId,
                                              ChangeStatusDto dto) {
        log.info("Technician {} changing status of request {} to {}", technicianId, requestId, dto.status());
        MaintenanceRequest existing = repo.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found: " + requestId));

        if (existing.getTechnician() == null ||
                !existing.getTechnician().getId().equals(technicianId)) {
            log.warn("Technician {} is not assigned to request {}", technicianId, requestId);
            throw new AccessDeniedException("Not assigned to you");
        }

        MaintenanceRequest updated = existing.toBuilder()
                .status(dto.status())
                .build();

        MaintenanceRequest saved = repo.save(updated);
        log.info("Technician {} changed status of request {} to {}", technicianId, requestId, dto.status());
        return mapper.toDto(saved);
    }
}