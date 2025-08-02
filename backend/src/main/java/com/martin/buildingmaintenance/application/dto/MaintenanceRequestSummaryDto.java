package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Summary information about a maintenance request, including related technician and resident.")
public record MaintenanceRequestSummaryDto(
    @Schema(description = "Unique identifier of the maintenance request.")
    UUID id,
    @Schema(description = "Status of the maintenance request.")
    String status,
    @Schema(description = "Date and time when the request was created.")
    LocalDateTime createdAt,
    @Schema(description = "Description of the maintenance request.")
    String description,
    @Schema(description = "Specialization required for the request.")
    String specialization,
    @Schema(description = "Scheduled date and time for the request.")
    LocalDateTime scheduledAt,
    @Schema(description = "Summary of the assigned technician.")
    TechnicianSummaryDto technician,
    @Schema(description = "Summary of the resident who made the request.")
    ResidentSummaryDto resident
) {}
