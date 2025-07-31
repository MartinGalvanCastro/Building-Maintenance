package com.martin.buildingmaintenance.domain.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    private  UUID id;
    private  String fullName;
    private  String email;
    private  String passwordHash;
    private  LocalDateTime createdAt;
    private  Role role;
}