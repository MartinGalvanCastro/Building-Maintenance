package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.CancelResponseDto;
import com.martin.buildingmaintenance.application.dto.CreateRequestDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.UpdateRequestDto;
import com.martin.buildingmaintenance.application.port.in.ResidentRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Resident Maintenance Requests",
        description = "Endpoints for residents to manage their own maintenance requests.")
@RestController
@RequestMapping("/residents/{residentId}/requests")
@PreAuthorize("hasRole('RESIDENT') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class ResidentRequestController {

    private final ResidentRequestService svc;

    @Operation(
            summary = "List all maintenance requests for a resident",
            description = "Returns a list of all maintenance requests for the specified resident.")
    @ApiResponse(
            responseCode = "200",
            description = "List of maintenance requests",
            content =
                    @Content(
                            mediaType = "application/json",
                            array =
                                    @ArraySchema(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    MaintenanceRequestDto.class))))
    @GetMapping
    public List<MaintenanceRequestDto> list(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable
                    UUID residentId) {
        return svc.listMyRequests(residentId);
    }

    @Operation(
            summary = "Get a maintenance request by ID for a resident",
            description =
                    "Returns a maintenance request by its unique identifier for the specified resident.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Maintenance request found",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @GetMapping("/{requestId}")
    public MaintenanceRequestDto get(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable
                    UUID residentId,
            @Parameter(description = "UUID of the maintenance request", required = true)
                    @PathVariable
                    UUID requestId) {
        return svc.getMyRequest(residentId, requestId);
    }

    @Operation(
            summary = "Create a new maintenance request for a resident",
            description = "Creates a new maintenance request for the specified resident.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Maintenance request created",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public MaintenanceRequestDto create(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable
                    UUID residentId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Maintenance request creation data",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    CreateRequestDto.class)))
                    @Valid
                    @RequestBody
                    CreateRequestDto dto) {
        return svc.createRequest(residentId, dto);
    }

    @Operation(
            summary = "Update a maintenance request for a resident",
            description = "Updates an existing maintenance request for the specified resident.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Maintenance request updated",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MaintenanceRequestDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @PatchMapping("/{requestId}")
    public MaintenanceRequestDto update(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable
                    UUID residentId,
            @Parameter(description = "UUID of the maintenance request", required = true)
                    @PathVariable
                    UUID requestId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Maintenance request update data",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    UpdateRequestDto.class)))
                    @Valid
                    @RequestBody
                    UpdateRequestDto dto) {
        return svc.updateMyRequest(residentId, requestId, dto);
    }

    @Operation(
            summary = "Cancel a maintenance request for a resident",
            description = "Cancels a maintenance request for the specified resident.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Maintenance request cancelled",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CancelResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Maintenance request not found")
    })
    @DeleteMapping("/{requestId}")
    public CancelResponseDto cancel(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable
                    UUID residentId,
            @Parameter(description = "UUID of the maintenance request", required = true)
                    @PathVariable
                    UUID requestId) {
        return svc.cancelMyRequest(residentId, requestId);
    }
}
