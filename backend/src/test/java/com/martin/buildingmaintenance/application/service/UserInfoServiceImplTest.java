package com.martin.buildingmaintenance.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.application.dto.ResidentialComplexDto;
import com.martin.buildingmaintenance.application.dto.UserInfoDto;
import com.martin.buildingmaintenance.application.exception.AccessDeniedException;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.application.port.out.ResidentRepository;
import com.martin.buildingmaintenance.application.port.out.TechnicianRepository;
import com.martin.buildingmaintenance.application.port.out.UserRepository;
import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.domain.model.ResidentialComplex;
import com.martin.buildingmaintenance.domain.model.Role;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentialComplexMapper;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceImplTest {
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock ResidentRepository residentRepository;
    @Mock TechnicianRepository technicianRepository;
    @Mock UserRepository userRepository;
    @Mock MaintenanceRequestRepository maintenanceRequestRepository;
    @Mock ResidentialComplexMapper residentialComplexMapper;
    @Mock MaintenanceRequestMapper maintenanceRequestMapper;
    @InjectMocks UserInfoServiceImpl service;

    private final String AUTH_HEADER = "Bearer token";
    private final UUID USER_ID = UUID.randomUUID();
    private Claims claims;
    private Jws<Claims> jws;

    @BeforeEach
    void setup() {
        claims = mock(Claims.class);
        jws = mock(Jws.class);
    }

    @Test
    void getCurrentUserInfo_resident_success() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(jws);
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(USER_ID.toString());
        when(claims.get("role")).thenReturn("RESIDENT");
        Resident resident = mock(Resident.class);
        ResidentialComplex residentialComplex = mock(ResidentialComplex.class);
        ResidentialComplexDto residentialComplexDto = mock(ResidentialComplexDto.class);
        when(residentRepository.findById(USER_ID)).thenReturn(Optional.of(resident));
        when(resident.getId()).thenReturn(USER_ID);
        when(resident.getFullName()).thenReturn("Resident Name");
        when(resident.getEmail()).thenReturn("resident@email.com");
        when(resident.getRole()).thenReturn(Role.RESIDENT);
        when(resident.getResidentialComplex()).thenReturn(residentialComplex);
        when(residentialComplexMapper.toDto(residentialComplex)).thenReturn(residentialComplexDto);
        when(maintenanceRequestRepository.findByResidentId(USER_ID))
                .thenReturn(Collections.emptyList());
        UserInfoDto dto = service.getCurrentUserInfo(AUTH_HEADER);
        assertEquals(USER_ID.toString(), dto.id());
        assertEquals(Role.RESIDENT, dto.role());
    }

    @Test
    void getCurrentUserInfo_admin_success() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(jws);
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(USER_ID.toString());
        when(claims.get("role")).thenReturn("ADMIN");
        User admin = mock(User.class);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(admin));
        when(admin.getId()).thenReturn(USER_ID);
        when(admin.getFullName()).thenReturn("Admin Name");
        when(admin.getEmail()).thenReturn("admin@email.com");
        when(admin.getRole()).thenReturn(Role.ADMIN);
        UserInfoDto dto = service.getCurrentUserInfo(AUTH_HEADER);
        assertEquals(USER_ID.toString(), dto.id());
        assertEquals(Role.ADMIN, dto.role());
    }

    @Test
    void getCurrentUserInfo_technician_success() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(jws);
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(USER_ID.toString());
        when(claims.get("role")).thenReturn("TECHNICIAN");
        Technician tech = mock(Technician.class);
        when(technicianRepository.findById(USER_ID)).thenReturn(Optional.of(tech));
        when(tech.getId()).thenReturn(USER_ID);
        when(tech.getFullName()).thenReturn("Tech Name");
        when(tech.getEmail()).thenReturn("tech@email.com");
        when(tech.getRole()).thenReturn(Role.TECHNICIAN);
        when(maintenanceRequestRepository.findByAssignedTechnicianId(USER_ID))
                .thenReturn(Collections.emptyList());
        UserInfoDto dto = service.getCurrentUserInfo(AUTH_HEADER);
        assertEquals(USER_ID.toString(), dto.id());
        assertEquals(Role.TECHNICIAN, dto.role());
    }

    @Test
    void getCurrentUserInfo_resident_not_found() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(jws);
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(USER_ID.toString());
        when(claims.get("role")).thenReturn("RESIDENT");
        when(residentRepository.findById(USER_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getCurrentUserInfo(AUTH_HEADER));
    }

    @Test
    void getCurrentUserInfo_invalid_header() {
        assertThrows(AccessDeniedException.class, () -> service.getCurrentUserInfo(null));
        assertThrows(AccessDeniedException.class, () -> service.getCurrentUserInfo("invalid"));
    }

    @Test
    void getCurrentUserInfo_unknown_role() {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(jws);
        when(jws.getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn(USER_ID.toString());
        when(claims.get("role")).thenReturn("UNKNOWN");
        assertThrows(AccessDeniedException.class, () -> service.getCurrentUserInfo(AUTH_HEADER));
    }
}
