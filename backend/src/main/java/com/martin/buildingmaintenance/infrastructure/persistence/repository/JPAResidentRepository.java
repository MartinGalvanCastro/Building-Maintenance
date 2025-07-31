package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JPAResidentRepository extends JpaRepository<ResidentEntity, UUID> {

    List<ResidentEntity> findByResidentialComplex_Id(UUID complexId);

}
