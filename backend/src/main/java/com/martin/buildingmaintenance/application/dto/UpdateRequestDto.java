package com.martin.buildingmaintenance.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UpdateRequestDto(
        @NotBlank(message = "Description must not be blank") String description,
        @NotNull(message = "Scheduled date must not be null") LocalDateTime scheduledAt) {}
