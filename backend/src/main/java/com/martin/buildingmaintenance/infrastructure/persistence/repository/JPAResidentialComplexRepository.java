package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentialComplexEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPAResidentialComplexRepository extends JpaRepository<ResidentialComplexEntity, UUID> {
}

