package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO containing user credentials for authentication.")
public record CredentialsDto(
        @Schema(description = "User's email address.", example = "user@example.com", required = true)
        @NotNull(message = "Email must not be null")
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid") String email,
        @Schema(description = "User's password.", example = "yourPassword", required = true)
        @NotNull(message = "Password must not be null")
        @NotBlank(message = "Password must not be blank") String password
) {}
