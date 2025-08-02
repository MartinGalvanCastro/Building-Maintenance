package com.martin.buildingmaintenance.application.dto;

import com.martin.buildingmaintenance.domain.model.Specialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TechnicianCreateCommandDto(
        @NotNull(message = "Full name must not be null")
                @NotBlank(message = "Full name must not be blank")
                String fullName,
        @NotNull(message = "Email must not be null") @NotBlank(message = "Email must not be blank")
                String email,
        @NotNull(message = "Password must not be null")
                @NotBlank(message = "Password must not be blank")
                String password,
        @NotNull(message = "Specializations must not be null")
                @NotEmpty(message = "Specializations must not be empty")
                List<@NotNull(message = "Specialization must not be null") Specialization>
                        specializations) {}
