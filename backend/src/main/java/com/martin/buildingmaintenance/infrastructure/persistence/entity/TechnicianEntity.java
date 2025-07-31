package com.martin.buildingmaintenance.infrastructure.persistence.entity;

import com.martin.buildingmaintenance.domain.model.Specialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "technician")
@DiscriminatorValue("TECHNICIAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianEntity extends UserEntity{

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "technician_specializations",
            joinColumns = @JoinColumn(name = "technician_id")
    )
    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Specialization> specializations;
}
