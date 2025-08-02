package com.martin.buildingmaintenance.application.port.out;

import com.martin.buildingmaintenance.domain.model.ResidentialComplex;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentialComplexRepository {
    List<ResidentialComplex> findAll();

    Optional<ResidentialComplex> findById(UUID id);

    ResidentialComplex save(ResidentialComplex complex);

    void deleteById(UUID id);
}
