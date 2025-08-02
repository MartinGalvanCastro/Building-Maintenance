package com.martin.buildingmaintenance.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description =
                "DTO returned after deleting a resource, containing a message about the operation result.")
public record DeleteResponseDto(
        @Schema(
                        description = "Message describing the result of the delete operation.",
                        example = "Residential complex deleted successfully.")
                String message) {}
