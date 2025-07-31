package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "DTO representing a residential complex, including its unique ID, name, address, city, and postal code.")
public record ResidentialComplexDto(
         @Schema(description = "Unique identifier of the residential complex.", example = "b3b6c7e2-8e2a-4b2a-9c2a-1b2a3c4d5e6f") UUID id,
         @Schema(description = "Name of the residential complex.", example = "Sunset Villas") String name,
         @Schema(description = "Address of the residential complex.", example = "123 Main St") String address,
         @Schema(description = "City where the residential complex is located.", example = "Springfield") String city,
         @Schema(description = "Postal code of the residential complex.", example = "12345") String postalCode
) {}
