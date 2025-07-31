package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.application.dto.ResidentDto;
import com.martin.buildingmaintenance.application.dto.ResidentSummaryDto;
import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses={
                ResidentialComplexMapper.class
        }
)
public interface ResidentMapper {

    @Mapping(target = "role", ignore = true)
    Resident toDomain(ResidentEntity e);

    @InheritInverseConfiguration
    ResidentEntity toEntity(Resident d);


    ResidentSummaryDto toSummaryDto(Resident d);


    ResidentDto toDto(Resident d);

}
