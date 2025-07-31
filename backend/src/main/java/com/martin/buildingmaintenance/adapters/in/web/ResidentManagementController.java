package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.ResidentCreateCommandDto;
import com.martin.buildingmaintenance.application.dto.ResidentDto;
import com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Resident Management", description = "Endpoints for managing residents. Only accessible by ADMIN users.")
@RestController
@RequestMapping("/residents")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ResidentManagementController {

    private final AdminManagementService adminSvc;

    @Operation(summary = "List all residents", description = "Returns a list of all residents.")
    @ApiResponse(responseCode = "200", description = "List of residents",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentDto.class)))
    @GetMapping
    public List<ResidentDto> listAll() {
        return adminSvc.listAllResidents();
    }

    @Operation(summary = "Get a resident by ID", description = "Returns a resident by their unique identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resident found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentDto.class))),
        @ApiResponse(responseCode = "404", description = "Resident not found")
    })
    @GetMapping("/{id}")
    public ResidentDto getOne(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable UUID id) {
        return adminSvc.getResident(id);
    }

    @Operation(summary = "Create a new resident", description = "Creates a new resident with the provided details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Resident created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidentDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Resident creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = ResidentCreateCommandDto.class))
            )
            @Valid @RequestBody ResidentCreateCommandDto dto
    ) {
        return adminSvc.saveResident(dto);
    }

    @Operation(summary = "Update a resident", description = "Updates an existing resident by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resident updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Resident not found")
    })
    @PutMapping("/{id}")
    public ResidentDto update(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Resident update data",
                required = true,
                content = @Content(schema = @Schema(implementation = ResidentUpdateCommandDto.class))
            )
            @Valid @RequestBody ResidentUpdateCommandDto dto
    ) {
        return adminSvc.updateResident(id, dto);
    }

    @Operation(summary = "Delete a resident", description = "Deletes a resident by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resident deleted",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Resident not found")
    })
    @DeleteMapping("/{id}")
    public DeleteResponseDto delete(
            @Parameter(description = "UUID of the resident", required = true) @PathVariable UUID id) {
        return adminSvc.deleteResident(id);
    }

}
