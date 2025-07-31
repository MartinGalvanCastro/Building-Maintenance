package com.martin.buildingmaintenance.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Technician extends User {

    private Set<Specialization> specializations;

    @Builder.Default
    private final Role role = Role.TECHNICIAN;

}
