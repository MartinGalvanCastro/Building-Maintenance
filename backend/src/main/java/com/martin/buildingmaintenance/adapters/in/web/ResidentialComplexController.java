package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.ResidentialComplexCommandDto;
import com.martin.buildingmaintenance.application.dto.ResidentialComplexDto;
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

@Tag(name = "Residential Complex Management", description = "Endpoints for managing residential complexes. Only accessible by ADMIN users.")
@RestController
@RequestMapping("/residential-complexes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ResidentialComplexController {

    private final AdminManagementService adminSvc;

    @Operation(summary = "List all residential complexes", description = "Returns a list of all residential complexes.")
    @ApiResponse(responseCode = "200", description = "List of residential complexes",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentialComplexDto.class)))
    @GetMapping
    public List<ResidentialComplexDto> listAll() {
        return adminSvc.listAllComplexes();
    }

    @Operation(summary = "Get a residential complex by ID", description = "Returns a residential complex by its unique identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Residential complex found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentialComplexDto.class))),
        @ApiResponse(responseCode = "404", description = "Residential complex not found")
    })
    @GetMapping("/{id}")
    public ResidentialComplexDto getOne(
            @Parameter(description = "UUID of the residential complex", required = true) @PathVariable UUID id) {
        return adminSvc.getComplex(id);
    }

    @Operation(summary = "Create a new residential complex", description = "Creates a new residential complex with the provided details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Residential complex created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentialComplexDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidentialComplexDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Residential complex creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = ResidentialComplexCommandDto.class))
            )
            @Valid @RequestBody ResidentialComplexCommandDto dto
    ) {
        return adminSvc.saveComplex(dto);
    }

    @Operation(summary = "Update a residential complex", description = "Updates an existing residential complex by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Residential complex updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResidentialComplexDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Residential complex not found")
    })
    @PutMapping("/{id}")
    public ResidentialComplexDto update(
            @Parameter(description = "UUID of the residential complex", required = true) @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Residential complex update data",
                required = true,
                content = @Content(schema = @Schema(implementation = ResidentialComplexCommandDto.class))
            )
            @Valid @RequestBody ResidentialComplexCommandDto dto
    ) {
        return adminSvc.updateComplex(id, dto);
    }

    @Operation(summary = "Delete a residential complex", description = "Deletes a residential complex by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Residential complex deleted",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Residential complex not found")
    })
    @DeleteMapping("/{id}")
    public DeleteResponseDto delete(
            @Parameter(description = "UUID of the residential complex", required = true) @PathVariable UUID id) {
        return adminSvc.deleteComplex(id);
    }
}