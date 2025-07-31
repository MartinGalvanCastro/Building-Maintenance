package com.martin.buildingmaintenance.application.dto;

import java.util.UUID;

public record TechnicianSummaryDto(
        UUID id,
        String fullName,
        String email
) {
}
