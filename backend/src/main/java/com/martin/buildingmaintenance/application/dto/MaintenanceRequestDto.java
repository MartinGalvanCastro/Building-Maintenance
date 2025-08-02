package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.RequestStatus;
import com.martin.buildingmaintenance.domain.model.Specialization;
import java.time.LocalDateTime;
import java.util.UUID;

public record MaintenanceRequestDto(
        UUID id,
        ResidentSummaryDto resident,
        String description,
        Specialization specialization,
        RequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime scheduledAt,
        LocalDateTime completedAt,
        TechnicianSummaryDto technician) {}
