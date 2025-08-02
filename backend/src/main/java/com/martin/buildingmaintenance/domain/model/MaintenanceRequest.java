package com.martin.buildingmaintenance.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {
    private UUID id;
    private Resident resident;
    private String description;
    private Specialization specialization;
    private LocalDateTime scheduledAt;
    private Technician technician;
    private LocalDateTime completedAt;

    @Builder.Default private final RequestStatus status = RequestStatus.PENDING;
    @Builder.Default private final LocalDateTime createdAt = LocalDateTime.now();
}
