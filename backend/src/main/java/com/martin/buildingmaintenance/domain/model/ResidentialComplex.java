package com.martin.buildingmaintenance.domain.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResidentialComplex {
    private UUID id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
}
