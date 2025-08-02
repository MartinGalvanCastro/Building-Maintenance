package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary information about a residential complex.")
public record ResidentialComplexDto(
    @Schema(description = "Unique identifier of the residential complex.")
    String id,
    @Schema(description = "Name of the residential complex.")
    String name,
    @Schema(description = "Address of the residential complex.")
    String address,
    @Schema(description = "City where the residential complex is located.")
    String city,
    @Schema(description = "Postal code of the residential complex.")
    String postalCode
) {}
