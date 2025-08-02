package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentialComplexEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAResidentialComplexRepository
        extends JpaRepository<ResidentialComplexEntity, UUID> {}
