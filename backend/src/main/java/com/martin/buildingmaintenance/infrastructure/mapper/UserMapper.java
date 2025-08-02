package com.martin.buildingmaintenance.infrastructure.mapper;

import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.TechnicianEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    Resident toDomain(ResidentEntity entity);

    @Mapping(target = "role", ignore = true)
    Technician toDomain(TechnicianEntity entity);

    @InheritInverseConfiguration
    ResidentEntity toEntity(Resident resident);

    @InheritInverseConfiguration
    TechnicianEntity toEntity(Technician technician);

    default User toDomain(UserEntity entity) {
        if (entity instanceof ResidentEntity) {
            return toDomain((ResidentEntity) entity);
        }
        if (entity instanceof TechnicianEntity) {
            return toDomain((TechnicianEntity) entity);
        }
        throw new IllegalArgumentException(
                "Unknown UserEntity type: " + entity.getClass().getName());
    }

    default UserEntity toEntity(User user) {
        if (user instanceof Resident) {
            return toEntity((Resident) user);
        }
        if (user instanceof Technician) {
            return toEntity((Technician) user);
        }
        throw new IllegalArgumentException("Unknown User type: " + user.getClass().getName());
    }
}
