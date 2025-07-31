package com.martin.buildingmaintenance.infrastructure.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.martin.buildingmaintenance.domain.model.Admin;
import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.AdminEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.TechnicianEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {
    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = mock(UserMapper.class, CALLS_REAL_METHODS);
    }

    @Test
    void toDomain_adminEntity() {
        AdminEntity entity = mock(AdminEntity.class);
        Admin admin = mock(Admin.class);
        when(mapper.toDomain(entity)).thenReturn(admin);
        User result = mapper.toDomain((UserEntity) entity);
        assertSame(admin, result);
        verify(mapper).toDomain(entity);
    }

    @Test
    void toDomain_residentEntity() {
        ResidentEntity entity = mock(ResidentEntity.class);
        Resident resident = mock(Resident.class);
        when(mapper.toDomain(entity)).thenReturn(resident);
        User result = mapper.toDomain((UserEntity) entity);
        assertSame(resident, result);
        verify(mapper).toDomain(entity);
    }

    @Test
    void toDomain_technicianEntity() {
        TechnicianEntity entity = mock(TechnicianEntity.class);
        Technician technician = mock(Technician.class);
        when(mapper.toDomain(entity)).thenReturn(technician);
        User result = mapper.toDomain((UserEntity) entity);
        assertSame(technician, result);
        verify(mapper).toDomain(entity);
    }

    @Test
    void toDomain_unknownEntity_throws() {
        UserEntity unknown = mock(UserEntity.class);
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(unknown));
    }

    @Test
    void toEntity_admin() {
        Admin admin = mock(Admin.class);
        AdminEntity entity = mock(AdminEntity.class);
        when(mapper.toEntity(admin)).thenReturn(entity);
        UserEntity result = mapper.toEntity((User) admin);
        assertSame(entity, result);
        verify(mapper).toEntity(admin);
    }

    @Test
    void toEntity_resident() {
        Resident resident = mock(Resident.class);
        ResidentEntity entity = mock(ResidentEntity.class);
        when(mapper.toEntity(resident)).thenReturn(entity);
        UserEntity result = mapper.toEntity((User) resident);
        assertSame(entity, result);
        verify(mapper).toEntity(resident);
    }

    @Test
    void toEntity_technician() {
        Technician technician = mock(Technician.class);
        TechnicianEntity entity = mock(TechnicianEntity.class);
        when(mapper.toEntity(technician)).thenReturn(entity);
        UserEntity result = mapper.toEntity((User) technician);
        assertSame(entity, result);
        verify(mapper).toEntity(technician);
    }

    @Test
    void toEntity_unknownUser_throws() {
        User unknown = mock(User.class);
        assertThrows(IllegalArgumentException.class, () -> mapper.toEntity(unknown));
    }
}