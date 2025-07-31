package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.AdminEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JPAAdminRepository extends JpaRepository<AdminEntity, UUID> {

}

