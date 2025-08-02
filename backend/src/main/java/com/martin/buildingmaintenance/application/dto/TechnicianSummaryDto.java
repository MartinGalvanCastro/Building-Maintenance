package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Summary information about a technician.")
public record TechnicianSummaryDto(
    @Schema(description = "Unique identifier of the technician.")
    UUID id,
    @Schema(description = "Full name of the technician.")
    String fullName,
    @Schema(description = "Email address of the technician.")
    String email
) {}
