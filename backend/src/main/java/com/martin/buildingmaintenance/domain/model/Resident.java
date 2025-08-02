package com.martin.buildingmaintenance.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Resident extends User {

    private String unitNumber;
    private String unitBlock;
    private ResidentialComplex residentialComplex;

    @Builder.Default private final Role role = Role.RESIDENT;
}
