package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Specialization;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Payload for creating a new maintenance request. Resident ID is required.")
public record CreateMaintenanceRequestDto(
        @Schema(description = "ID of the resident making the request", required = true, example = "b1a7e8c2-1234-4f8a-9b2e-123456789abc")
        @NotNull(message = "Resident ID must not be null")
        UUID residentId,
        @Schema(description = "Description of the maintenance issue", required = true, example = "Leaking kitchen faucet")
        @NotNull(message = "Description must not be null")
        @NotBlank(message = "Description must not be blank")
        String description,
        @Schema(description = "Required technician specialization", required = true, example = "PLUMBING")
        @NotNull(message = "Required specialization must not be null")
        Specialization specialization,
        @Schema(description = "Scheduled date and time for the maintenance", required = true, example = "2025-08-01T10:00:00")
        @NotNull(message = "Scheduled date must not be null")
        @Future(message = "Scheduled date must be in the future")
        LocalDateTime scheduledAt
) {}
