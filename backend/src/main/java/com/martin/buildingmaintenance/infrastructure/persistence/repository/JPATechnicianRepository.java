package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.TechnicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JPATechnicianRepository extends JpaRepository<TechnicianEntity, UUID> {

    List<TechnicianEntity> findBySpecializationsContaining(Specialization specialization);

}

