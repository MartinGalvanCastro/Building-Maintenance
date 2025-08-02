package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusDto(@NotNull(message = "Status must not be null") RequestStatus status) {}
