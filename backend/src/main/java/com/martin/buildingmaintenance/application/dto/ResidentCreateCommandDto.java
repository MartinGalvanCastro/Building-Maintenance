package com.martin.buildingmaintenance.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ResidentCreateCommandDto(
        @NotNull(message = "Full name must not be null")
                @NotBlank(message = "Full name must not be blank")
                String fullName,
        @NotNull(message = "Email must not be null")
                @NotBlank(message = "Email must not be blank")
                @Email(message = "Email must be valid")
                String email,
        @NotNull(message = "Password must not be null")
                @NotBlank(message = "Password must not be blank")
                String password,
        @NotNull(message = "Unit number must not be null")
                @NotBlank(message = "Unit number must not be blank")
                String unitNumber,
        @NotNull(message = "Unit block must not be null")
                @NotBlank(message = "Unit block must not be blank")
                String unitBlock,
        @NotNull(message = "Residential complex ID must not be null") UUID residentialComplexId) {}
