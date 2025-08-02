package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.application.dto.TechnicianDto;
import com.martin.buildingmaintenance.application.dto.TechnicianSummaryDto;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.TechnicianEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TechnicianMapper {

    @Mapping(target = "role", ignore = true)
    Technician toDomain(TechnicianEntity e);

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id")
    TechnicianEntity toEntity(Technician d);

    TechnicianSummaryDto toSummaryDto(TechnicianEntity entity);

    default TechnicianDto toDto(Technician d) {
        return new TechnicianDto(
                d.getId(), d.getFullName(), d.getEmail(), d.getSpecializations().stream().toList());
    }
}
