package com.martin.buildingmaintenance.domain.model;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Technician extends User {

    private Set<Specialization> specializations;

    @Builder.Default private final Role role = Role.TECHNICIAN;
}
