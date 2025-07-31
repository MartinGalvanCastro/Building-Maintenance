package com.martin.buildingmaintenance.application.port.out;

import com.martin.buildingmaintenance.domain.model.Resident;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentRepository {
    List<Resident> findAll();
    Optional<Resident> findById(UUID id);
    Resident save(Resident e);
    void deleteById(UUID id);
}
