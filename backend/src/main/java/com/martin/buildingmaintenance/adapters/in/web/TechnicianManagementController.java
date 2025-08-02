package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.TechnicianCreateCommandDto;
import com.martin.buildingmaintenance.application.dto.TechnicianDto;
import com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Technician Management",
        description = "Endpoints for managing technicians. Only accessible by ADMIN users.")
@RestController
@RequestMapping("/technicians")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class TechnicianManagementController {

    private final AdminManagementService adminSvc;

    @Operation(summary = "List all technicians", description = "Returns a list of all technicians.")
    @ApiResponse(
            responseCode = "200",
            description = "List of technicians",
            content =
                    @Content(
                            mediaType = "application/json",
                            array =
                                    @ArraySchema(
                                            schema =
                                                    @Schema(implementation = TechnicianDto.class))))
    @GetMapping
    public List<TechnicianDto> listAll() {
        return adminSvc.listAllTechnicians();
    }

    @Operation(
            summary = "Get a technician by ID",
            description = "Returns a technician by their unique identifier.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Technician found",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TechnicianDto.class))),
        @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    @GetMapping("/{id}")
    public TechnicianDto getOne(
            @Parameter(description = "UUID of the technician", required = true) @PathVariable
                    UUID id) {
        return adminSvc.getTechnician(id);
    }

    @Operation(
            summary = "Create a new technician",
            description = "Creates a new technician with the provided details.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Technician created",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TechnicianDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TechnicianDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Technician creation data",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    TechnicianCreateCommandDto
                                                                            .class)))
                    @Valid
                    @RequestBody
                    TechnicianCreateCommandDto dto) {
        return adminSvc.saveTechnician(dto);
    }

    @Operation(
            summary = "Update a technician",
            description = "Updates an existing technician by their ID.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Technician updated",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TechnicianDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    @PutMapping("/{id}")
    public TechnicianDto update(
            @Parameter(description = "UUID of the technician", required = true) @PathVariable
                    UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Technician update data",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    TechnicianUpdateCommandDto
                                                                            .class)))
                    @Valid
                    @RequestBody
                    TechnicianUpdateCommandDto dto) {
        return adminSvc.updateTechnician(id, dto);
    }

    @Operation(summary = "Delete a technician", description = "Deletes a technician by their ID.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Technician deleted",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = DeleteResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    @DeleteMapping("/{id}")
    public DeleteResponseDto delete(
            @Parameter(description = "UUID of the technician", required = true) @PathVariable
                    UUID id) {
        return adminSvc.deleteTechnician(id);
    }
}
