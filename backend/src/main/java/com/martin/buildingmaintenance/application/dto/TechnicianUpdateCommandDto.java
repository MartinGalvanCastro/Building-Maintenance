package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Specialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TechnicianUpdateCommandDto(
        @NotBlank(message = "Full name must not be blank") String fullName,
        @NotBlank(message = "Email must not be blank") String email,
        String password,
        @NotEmpty(message = "Specializations must not be empty")
                List<Specialization> specializations) {}
