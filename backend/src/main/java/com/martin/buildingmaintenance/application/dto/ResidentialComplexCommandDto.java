package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO used to create or update a residential complex.")
public record ResidentialComplexCommandDto(
        @Schema(description = "Name of the residential complex.", example = "Sunset Villas", required = true)
        @NotNull(message = "Name must not be null")
        @NotBlank(message = "Name must not be blank") String name,
        @Schema(description = "Address of the residential complex.", example = "123 Main St", required = true)
        @NotNull(message = "Address must not be null")
        @NotBlank(message = "Address must not be blank") String address,
        @Schema(description = "City where the residential complex is located.", example = "Springfield", required = true)
        @NotNull(message = "City must not be null")
        @NotBlank(message = "City must not be blank") String city,
        @Schema(description = "Postal code of the residential complex.", example = "12345", required = true)
        @NotNull(message = "Postal code must not be null")
        @NotBlank(message = "Postal code must not be blank") String postalCode
) {}
