package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.application.dto.ResidentialComplexDto;
import com.martin.buildingmaintenance.domain.model.ResidentialComplex;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentialComplexEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ResidentialComplexMapper {

    ResidentialComplex toDomain(ResidentialComplexEntity e);

    ResidentialComplexEntity toEntity(ResidentialComplex d);

    ResidentialComplexDto toDto(ResidentialComplex d);
}
