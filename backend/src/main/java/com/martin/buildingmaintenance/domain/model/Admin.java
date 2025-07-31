package com.martin.buildingmaintenance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Admin extends User {


    @Builder.Default
    private final Role role = Role.ADMIN;
}
