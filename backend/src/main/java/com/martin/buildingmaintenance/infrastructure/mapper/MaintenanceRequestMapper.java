package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestSummaryDto;
import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.MaintenanceRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {
            ResidentMapper.class,
            TechnicianMapper.class,
        })
public interface MaintenanceRequestMapper {
    MaintenanceRequest toDomain(MaintenanceRequestEntity e);

    MaintenanceRequestEntity toEntity(MaintenanceRequest d);

    MaintenanceRequestDto toDto(MaintenanceRequest d);

    MaintenanceRequestSummaryDto toSummary(MaintenanceRequest d);
}
