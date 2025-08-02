package com.martin.buildingmaintenance.application.port.out;

import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.domain.model.Technician;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TechnicianRepository {
    List<Technician> findAll();

    Optional<Technician> findById(UUID id);

    List<Technician> findBySpecialization(Specialization specialization);

    Technician save(Technician technician);

    void deleteById(UUID id);
}
