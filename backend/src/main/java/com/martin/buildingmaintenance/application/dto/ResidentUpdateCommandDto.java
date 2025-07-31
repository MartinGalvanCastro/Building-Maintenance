package com.martin.buildingmaintenance.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ResidentUpdateCommandDto(
        @NotBlank(message = "Full name must not be blank") String fullName,
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid") String email,
        String password,
        @NotBlank(message = "Unit number must not be blank") String unitNumber,
        @NotBlank(message = "Unit block must not be blank") String unitBlock,
        UUID residentialComplexId
) {}

