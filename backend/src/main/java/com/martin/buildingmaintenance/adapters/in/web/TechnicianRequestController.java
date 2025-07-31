package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.ChangeStatusDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.port.in.TechnicianRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name = "Technician Maintenance Requests", description = "Endpoints for technicians to manage their assigned maintenance requests.")
@RestController
@RequestMapping("/technicians/{technicianId}/requests")
@PreAuthorize("hasRole('TECHNICIAN') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class TechnicianRequestController {

    private final TechnicianRequestService svc;

    @Operation(summary = "List all maintenance requests assigned to a technician", description = "Returns a list of all maintenance requests assigned to the specified technician.")
    @ApiResponse(responseCode = "200", description = "List of maintenance requests",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaintenanceRequestDto.class)))
    @GetMapping
    public List<MaintenanceRequestDto> list(
            @Parameter(description = "UUID of the technician", required = true) @PathVariable UUID technicianId
    ) {
        return svc.listAssignments(technicianId);
    }

    @Operation(summary = "Change the status of a maintenance request", description = "Allows a technician to change the status of an assigned maintenance request.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status changed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @PatchMapping("/{requestId}/status")
    public MaintenanceRequestDto changeStatus(
            @Parameter(description = "UUID of the technician", required = true) @PathVariable UUID technicianId,
            @Parameter(description = "UUID of the maintenance request", required = true) @PathVariable UUID requestId,
            @Valid @RequestBody ChangeStatusDto dto
    ) {
        return svc.changeStatus(technicianId, requestId, dto);
    }
}