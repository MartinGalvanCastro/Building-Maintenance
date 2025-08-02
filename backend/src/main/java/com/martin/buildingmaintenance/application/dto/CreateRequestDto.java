package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Specialization;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateRequestDto(
        @NotNull(message = "Description must not be null")
                @NotBlank(message = "Description must not be blank")
                String description,
        @NotNull(message = "Required specialization must not be null")
                Specialization specialization,
        @NotNull(message = "Scheduled date must not be null")
                @Future(message = "Scheduled date must be in the future")
                LocalDateTime scheduledAt) {}
