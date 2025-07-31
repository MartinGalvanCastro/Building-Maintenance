package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CancelResponseDto(
        String         message
) {}