package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.domain.model.Admin;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.AdminEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "role", ignore = true)
    Admin toDomain(AdminEntity e);

    @InheritInverseConfiguration
    AdminEntity toEntity(Admin d);
}
