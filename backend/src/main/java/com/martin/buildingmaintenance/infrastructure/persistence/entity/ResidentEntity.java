package com.martin.buildingmaintenance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resident")
@DiscriminatorValue("RESIDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResidentEntity extends UserEntity{

    @Column(name = "unit_number", nullable = false)
    private String unitNumber;

    @Column(name = "unit_block", nullable = false)
    private String unitBlock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplexEntity residentialComplex;
}
