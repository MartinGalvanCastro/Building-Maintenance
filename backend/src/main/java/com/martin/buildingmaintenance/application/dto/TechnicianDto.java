package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Specialization;
import java.util.List;
import java.util.UUID;

public record TechnicianDto(
        UUID id, String fullName, String email, List<Specialization> specializations) {}
