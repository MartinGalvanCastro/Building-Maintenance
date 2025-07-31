package com.martin.buildingmaintenance.adapters.in.web;


import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Maintenance Request Management", description = "Endpoints for managing maintenance requests. Only accessible by ADMIN users.")
@RestController
@RequestMapping("/maintenance-requests")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MaintenanceRequestController {

    private final AdminManagementService adminSvc;

    @Operation(summary = "List all maintenance requests", description = "Returns a list of all maintenance requests.")
    @ApiResponse(responseCode = "200", description = "List of maintenance requests",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaintenanceRequestDto.class)))
    @GetMapping
    public List<MaintenanceRequestDto> listAll() {
        return adminSvc.listAllRequests();
    }

    @Operation(summary = "Get a maintenance request by ID", description = "Returns a maintenance request by its unique identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance request found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @GetMapping("/{id}")
    public MaintenanceRequestDto getOne(
            @Parameter(description = "UUID of the maintenance request", required = true) @PathVariable UUID id) {
        return adminSvc.getRequest(id);
    }

    @Operation(summary = "Assign a technician to a maintenance request", description = "Assigns a technician to a maintenance request by their IDs.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Technician assigned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance request or technician not found")
    })
    @PatchMapping("/{id}/technician/{technicianId}")
    public MaintenanceRequestDto assign(
            @Parameter(description = "UUID of the maintenance request", required = true) @PathVariable UUID id,
            @Parameter(description = "UUID of the technician", required = true) @PathVariable UUID technicianId
    ) {
        return adminSvc.assignTechnician(id, technicianId);
    }

    @Operation(summary = "Delete a maintenance request", description = "Deletes a maintenance request by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Maintenance request deleted",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @DeleteMapping("/{id}")
    public DeleteResponseDto delete(
            @Parameter(description = "UUID of the maintenance request", required = true) @PathVariable UUID id) {
        return adminSvc.deleteRequest(id);
    }
}
