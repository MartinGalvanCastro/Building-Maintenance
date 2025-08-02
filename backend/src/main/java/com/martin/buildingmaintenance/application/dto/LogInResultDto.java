package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO returned after successful authentication, containing the JWT token.")
public record LogInResultDto(
        @Schema(
                        description = "JWT token for authenticated user.",
                        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                String token) {}
