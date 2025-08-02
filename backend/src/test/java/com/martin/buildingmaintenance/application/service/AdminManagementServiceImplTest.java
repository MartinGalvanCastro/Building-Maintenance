package com.martin.buildingmaintenance.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.application.dto.*;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.out.*;
import com.martin.buildingmaintenance.domain.model.*;
import com.martin.buildingmaintenance.infrastructure.mapper.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminManagementServiceImplTest {
    @Mock ResidentialComplexRepository complexRepo;
    @Mock ResidentRepository residentRepo;
    @Mock TechnicianRepository technicianRepo;
    @Mock MaintenanceRequestRepository requestRepo;
    @Mock ResidentialComplexMapper complexMapper;
    @Mock ResidentMapper residentMapper;
    @Mock TechnicianMapper technicianMapper;
    @Mock MaintenanceRequestMapper requestMapper;
    @Mock UserRepository userRepo;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks AdminManagementServiceImpl service;

    @Test
    void listAllComplexes_returnsMappedList() {
        ResidentialComplex complex = mock(ResidentialComplex.class);
        when(complexRepo.findAll()).thenReturn(List.of(complex));
        var result = service.listAllComplexes();
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    void getComplex_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        ResidentialComplex complex = mock(ResidentialComplex.class);
        when(complexRepo.findById(id)).thenReturn(Optional.of(complex));
        assertNull(service.getComplex(id));
    }

    @Test
    void getComplex_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(complexRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getComplex(id));
    }

    @Test
    void saveComplex_success() {
        ResidentialComplexCommandDto dto = mock(ResidentialComplexCommandDto.class);
        ResidentialComplex domain =
                ResidentialComplex.builder()
                        .name("n")
                        .address("a")
                        .city("c")
                        .postalCode("p")
                        .build();
        ResidentialComplex saved = mock(ResidentialComplex.class);
        ResidentialComplexDto outDto = mock(ResidentialComplexDto.class);
        when(dto.name()).thenReturn("n");
        when(dto.address()).thenReturn("a");
        when(dto.city()).thenReturn("c");
        when(dto.postalCode()).thenReturn("p");
        when(complexRepo.save(any())).thenReturn(saved);
        assertNull(service.saveComplex(dto));
    }

    @Test
    void updateComplex_success() {
        UUID id = UUID.randomUUID();
        ResidentialComplex existing =
                ResidentialComplex.builder()
                        .name("n")
                        .address("a")
                        .city("c")
                        .postalCode("p")
                        .build();
        ResidentialComplexCommandDto dto = mock(ResidentialComplexCommandDto.class);
        ResidentialComplex saved = mock(ResidentialComplex.class);
        ResidentialComplexDto outDto = mock(ResidentialComplexDto.class);
        when(complexRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.name()).thenReturn("n2");
        when(dto.address()).thenReturn("a2");
        when(dto.city()).thenReturn("c2");
        when(dto.postalCode()).thenReturn("p2");
        when(complexRepo.save(any())).thenReturn(saved);
        assertNull(service.updateComplex(id, dto));
    }

    @Test
    void updateComplex_notFound_throws() {
        UUID id = UUID.randomUUID();
        ResidentialComplexCommandDto dto = mock(ResidentialComplexCommandDto.class);
        when(complexRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateComplex(id, dto));
    }

    @Test
    void deleteComplex_success() {
        UUID id = UUID.randomUUID();
        var result = service.deleteComplex(id);
        assertEquals("Residential complex deleted: " + id, result.message());
        verify(complexRepo).deleteById(id);
    }

    @Test
    void deleteComplex_notFound_throws() {
        UUID id = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(complexRepo).deleteById(id);
        assertThrows(NotFoundException.class, () -> service.deleteComplex(id));
    }

    // --- Residents ---
    @Test
    void listAllResidents_returnsMappedList() {
        Resident resident = mock(Resident.class);
        ResidentDto dto = mock(ResidentDto.class);
        when(residentRepo.findAll()).thenReturn(List.of(resident));
        when(residentMapper.toDto(resident)).thenReturn(dto);
        var result = service.listAllResidents();
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getResident_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        Resident resident = mock(Resident.class);
        ResidentDto dto = mock(ResidentDto.class);
        when(residentRepo.findById(id)).thenReturn(Optional.of(resident));
        when(residentMapper.toDto(resident)).thenReturn(dto);
        assertSame(dto, service.getResident(id));
    }

    @Test
    void getResident_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(residentRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getResident(id));
    }

    @Test
    void saveResident_success() {
        ResidentCreateCommandDto dto = mock(ResidentCreateCommandDto.class);
        Resident domain = Resident.builder()
                .fullName("f")
                .email("e")
                .passwordHash("p")
                .unitNumber("u")
                .unitBlock("b")
                .build();
        Resident saved = mock(Resident.class);
        ResidentialComplex complex = mock(ResidentialComplex.class);
        when(dto.fullName()).thenReturn("f");
        when(dto.email()).thenReturn("e");
        when(dto.password()).thenReturn("p");
        when(passwordEncoder.encode("p")).thenReturn("encoded-p");
        when(dto.unitNumber()).thenReturn("u");
        when(dto.unitBlock()).thenReturn("b");
        when(dto.residentialComplexId()).thenReturn(UUID.randomUUID());
        when(complexRepo.findById(any())).thenReturn(Optional.of(complex));
        when(userRepo.findByEmail("e")).thenReturn(Optional.empty());
        when(residentRepo.save(any())).thenReturn(saved);
        assertNull(service.saveResident(dto));
    }

    @Test
    void saveResident_complexNotFound_throws() {
        ResidentCreateCommandDto dto = mock(ResidentCreateCommandDto.class);
        when(dto.residentialComplexId()).thenReturn(UUID.randomUUID());
        when(complexRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.saveResident(dto));
    }

    @Test
    void updateResident_success() {
        UUID id = UUID.randomUUID();
        Resident existing = Resident.builder()
                .fullName("f")
                .email("e")
                .passwordHash("p")
                .unitNumber("u")
                .unitBlock("b")
                .build();
        ResidentUpdateCommandDto dto = mock(ResidentUpdateCommandDto.class);
        ResidentialComplex complex = mock(ResidentialComplex.class);
        when(residentRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.fullName()).thenReturn("f2");
        when(dto.email()).thenReturn("e2");
        when(dto.password()).thenReturn("p2");
        when(dto.unitNumber()).thenReturn("u2");
        when(dto.unitBlock()).thenReturn("b2");
        when(dto.residentialComplexId()).thenReturn(UUID.randomUUID());
        when(complexRepo.findById(any())).thenReturn(Optional.of(complex));
        assertNull(service.updateResident(id, dto));
    }

    @Test
    void updateResident_notFound_throws() {
        UUID id = UUID.randomUUID();
        ResidentUpdateCommandDto dto = mock(ResidentUpdateCommandDto.class);
        when(residentRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateResident(id, dto));
    }

    @Test
    void updateResident_complexNotFound_throws() {
        UUID id = UUID.randomUUID();
        Resident existing =
                Resident.builder()
                        .fullName("f")
                        .email("e")
                        .passwordHash("p")
                        .unitNumber("u")
                        .unitBlock("b")
                        .build();
        ResidentUpdateCommandDto dto = mock(ResidentUpdateCommandDto.class);
        when(residentRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.residentialComplexId()).thenReturn(UUID.randomUUID());
        when(complexRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateResident(id, dto));
    }

    @Test
    void updateResident_doesNotUpdatePasswordIfNullOrBlank() {
        UUID id = UUID.randomUUID();
        Resident existing = Resident.builder()
                .fullName("f")
                .email("e")
                .passwordHash("oldpass")
                .unitNumber("u")
                .unitBlock("b")
                .build();
        ResidentUpdateCommandDto dto = mock(ResidentUpdateCommandDto.class);
        Resident saved = mock(Resident.class);
        ResidentialComplex complex = mock(ResidentialComplex.class);
        when(residentRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.fullName()).thenReturn("f2");
        when(dto.email()).thenReturn("e2");
        when(dto.password()).thenReturn(null); // null password
        when(dto.unitNumber()).thenReturn("u2");
        when(dto.unitBlock()).thenReturn("b2");
        when(dto.residentialComplexId()).thenReturn(UUID.randomUUID());
        when(complexRepo.findById(any())).thenReturn(Optional.of(complex));
        when(residentRepo.save(any())).thenReturn(saved);
        assertNull(service.updateResident(id, dto));
        verify(residentRepo).save(argThat(res -> "oldpass".equals(res.getPasswordHash())));

        // blank password
        when(dto.password()).thenReturn("");
        service.updateResident(id, dto);
        verify(residentRepo, times(2)).save(argThat(res -> "oldpass".equals(res.getPasswordHash())));
    }

    @Test
    void deleteResident_success() {
        UUID id = UUID.randomUUID();
        var result = service.deleteResident(id);
        assertEquals("Resident deleted: " + id, result.message());
        verify(residentRepo).deleteById(id);
    }

    @Test
    void deleteResident_notFound_throws() {
        UUID id = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(residentRepo).deleteById(id);
        assertThrows(NotFoundException.class, () -> service.deleteResident(id));
    }

    // --- Technicians ---
    @Test
    void listAllTechnicians_returnsMappedList() {
        Technician tech = mock(Technician.class);
        TechnicianDto dto = mock(TechnicianDto.class);
        when(technicianRepo.findAll()).thenReturn(List.of(tech));
        when(technicianMapper.toDto(tech)).thenReturn(dto);
        var result = service.listAllTechnicians();
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getTechnician_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        Technician tech = mock(Technician.class);
        TechnicianDto dto = mock(TechnicianDto.class);
        when(technicianRepo.findById(id)).thenReturn(Optional.of(tech));
        when(technicianMapper.toDto(tech)).thenReturn(dto);
        assertSame(dto, service.getTechnician(id));
    }

    @Test
    void getTechnician_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(technicianRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getTechnician(id));
    }

    @Test
    void saveTechnician_success() {
        TechnicianCreateCommandDto dto = mock(TechnicianCreateCommandDto.class);
        Technician domain =
                Technician.builder()
                        .fullName("f")
                        .email("e")
                        .passwordHash("p")
                        .specializations(Set.of())
                        .build();
        Technician saved = mock(Technician.class);
        TechnicianDto outDto = mock(TechnicianDto.class);
        when(dto.fullName()).thenReturn("f");
        when(dto.email()).thenReturn("e");
        when(dto.password()).thenReturn("p");
        when(passwordEncoder.encode("p")).thenReturn("encoded-p");
        when(dto.specializations()).thenReturn(List.of());
        when(technicianRepo.save(any())).thenReturn(saved);
        assertNull(service.saveTechnician(dto));
    }

    @Test
    void updateTechnician_success() {
        UUID id = UUID.randomUUID();
        Technician existing =
                Technician.builder()
                        .fullName("f")
                        .email("e")
                        .passwordHash("p")
                        .specializations(Set.of())
                        .build();
        TechnicianUpdateCommandDto dto = mock(TechnicianUpdateCommandDto.class);
        when(technicianRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.fullName()).thenReturn("f2");
        when(dto.email()).thenReturn("e2");
        when(dto.password()).thenReturn("p2");
        when(dto.specializations()).thenReturn(List.of());
        assertNull(service.updateTechnician(id, dto));
    }

    @Test
    void updateTechnician_doesNotUpdatePasswordIfNullOrBlank() {
        UUID id = UUID.randomUUID();
        Technician existing = Technician.builder()
                .fullName("f")
                .email("e")
                .passwordHash("oldpass")
                .specializations(Set.of())
                .build();
        TechnicianUpdateCommandDto dto = mock(TechnicianUpdateCommandDto.class);
        Technician saved = mock(Technician.class);
        TechnicianDto outDto = mock(TechnicianDto.class);
        when(technicianRepo.findById(id)).thenReturn(Optional.of(existing));
        when(dto.fullName()).thenReturn("f2");
        when(dto.email()).thenReturn("e2");
        when(dto.password()).thenReturn(null); // null password
        when(dto.specializations()).thenReturn(List.of());
        assertNull(service.updateTechnician(id, dto));
        verify(technicianRepo).save(argThat(tech -> "oldpass".equals(tech.getPasswordHash())));

        // blank password
        when(dto.password()).thenReturn("");
        service.updateTechnician(id, dto);
        verify(technicianRepo, times(2)).save(argThat(tech -> "oldpass".equals(tech.getPasswordHash())));
    }

    @Test
    void deleteTechnician_success() {
        UUID id = UUID.randomUUID();
        var result = service.deleteTechnician(id);
        assertEquals("Technician deleted: " + id, result.message());
        verify(technicianRepo).deleteById(id);
    }

    @Test
    void deleteTechnician_notFound_throws() {
        UUID id = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(technicianRepo).deleteById(id);
        assertThrows(NotFoundException.class, () -> service.deleteTechnician(id));
    }

    // --- Maintenance Requests ---
    @Test
    void listAllRequests_returnsMappedList() {
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findAll()).thenReturn(List.of(req));
        when(requestMapper.toDto(req)).thenReturn(dto);
        var result = service.listAllRequests();
        assertEquals(1, result.size());
        assertSame(dto, result.get(0));
    }

    @Test
    void getRequest_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        MaintenanceRequest req = mock(MaintenanceRequest.class);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findById(id)).thenReturn(Optional.of(req));
        when(requestMapper.toDto(req)).thenReturn(dto);
        assertSame(dto, service.getRequest(id));
    }

    @Test
    void getRequest_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(requestRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getRequest(id));
    }

    @Test
    void assignTechnician_success() {
        UUID reqId = UUID.randomUUID();
        UUID techId = UUID.randomUUID();
        MaintenanceRequest existing =
                MaintenanceRequest.builder().id(reqId).status(RequestStatus.PENDING).build();
        Technician tech = Technician.builder().id(techId).build();
        MaintenanceRequest saved = mock(MaintenanceRequest.class);
        MaintenanceRequestDto outDto = mock(MaintenanceRequestDto.class);
        when(requestRepo.findById(reqId)).thenReturn(Optional.of(existing));
        when(technicianRepo.findById(techId)).thenReturn(Optional.of(tech));
        when(requestRepo.save(any())).thenReturn(saved);
        when(requestMapper.toDto(saved)).thenReturn(outDto);
        assertSame(outDto, service.assignTechnician(reqId, techId));
    }

    @Test
    void assignTechnician_requestNotFound_throws() {
        UUID reqId = UUID.randomUUID();
        UUID techId = UUID.randomUUID();
        when(requestRepo.findById(reqId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.assignTechnician(reqId, techId));
    }

    @Test
    void assignTechnician_technicianNotFound_throws() {
        UUID reqId = UUID.randomUUID();
        UUID techId = UUID.randomUUID();
        MaintenanceRequest existing =
                MaintenanceRequest.builder().id(reqId).status(RequestStatus.PENDING).build();
        when(requestRepo.findById(reqId)).thenReturn(Optional.of(existing));
        when(technicianRepo.findById(techId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.assignTechnician(reqId, techId));
    }

    @Test
    void deleteRequest_success() {
        UUID id = UUID.randomUUID();
        var result = service.deleteRequest(id);
        assertEquals("Request deleted: " + id, result.message());
        verify(requestRepo).deleteById(id);
    }

    @Test
    void deleteRequest_notFound_throws() {
        UUID id = UUID.randomUUID();
        doThrow(new NotFoundException("Not found")).when(requestRepo).deleteById(id);
        assertThrows(NotFoundException.class, () -> service.deleteRequest(id));
    }

    @Test
    void createRequest_success_withResident() {
        UUID residentId = UUID.randomUUID();
        CreateMaintenanceRequestDto dto = new CreateMaintenanceRequestDto(
            residentId, "desc", Specialization.PLUMBING, java.time.LocalDateTime.now().plusDays(1));
        Resident resident = mock(Resident.class);
        MaintenanceRequest saved = mock(MaintenanceRequest.class);
        MaintenanceRequestDto outDto = mock(MaintenanceRequestDto.class);
        when(residentRepo.findById(residentId)).thenReturn(Optional.of(resident));
        when(requestRepo.save(any())).thenReturn(saved);
        when(requestMapper.toDto(saved)).thenReturn(outDto);
        assertSame(outDto, service.createRequest(dto));
    }

    @Test
    void createRequest_residentNotFound_throws() {
        UUID residentId = UUID.randomUUID();
        CreateMaintenanceRequestDto dto = new CreateMaintenanceRequestDto(
            residentId, "desc", Specialization.PLUMBING, java.time.LocalDateTime.now().plusDays(1));
        when(residentRepo.findById(residentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.createRequest(dto));
    }

    @Test
    void createRequest_nullResidentId_throws() {
        CreateMaintenanceRequestDto dto = new CreateMaintenanceRequestDto(
            null, "desc", Specialization.PLUMBING, java.time.LocalDateTime.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.createRequest(dto));
    }
}
