package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO containing user info, relationships, and related maintenance requests for the /me endpoint.")
public record UserInfoDto(
    @Schema(description = "Unique identifier of the user.")
    String id,
    @Schema(description = "Full name of the user.")
    String name,
    @Schema(description = "Email address of the user.")
    String email,
    @Schema(description = "Role of the user (e.g., RESIDENT, ADMIN, TECHNICIAN).")
    Role role,
    @Schema(description = "Residential complex info (only for residents).")
    ResidentialComplexDto residentialComplex,
    @Schema(description = "List of related maintenance requests.")
    List<MaintenanceRequestSummaryDto> maintenanceRequests
) {}
