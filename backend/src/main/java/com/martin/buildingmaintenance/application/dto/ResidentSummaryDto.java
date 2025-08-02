package com.martin.buildingmaintenance.application.dto;

import java.util.UUID;

public record ResidentSummaryDto(
        UUID id,
        String fullName,
        String email,
        String unitNumber,
        String unitBlock,
        ResidentialComplexDto residentialComplex) {}
