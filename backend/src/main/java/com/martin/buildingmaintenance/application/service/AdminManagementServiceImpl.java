package com.martin.buildingmaintenance.application.service;

import com.martin.buildingmaintenance.application.dto.*;
import com.martin.buildingmaintenance.application.exception.EmailAlreadyExistsException;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import com.martin.buildingmaintenance.application.port.out.*;
import com.martin.buildingmaintenance.domain.model.*;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentMapper;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentialComplexMapper;
import com.martin.buildingmaintenance.infrastructure.mapper.TechnicianMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminManagementServiceImpl implements AdminManagementService {

    private final ResidentialComplexRepository complexRepo;
    private final ResidentRepository residentRepo;
    private final TechnicianRepository technicianRepo;
    private final MaintenanceRequestRepository requestRepo;
    private final UserRepository userRepo;

    private final ResidentialComplexMapper complexMapper;
    private final ResidentMapper residentMapper;
    private final TechnicianMapper technicianMapper;
    private final MaintenanceRequestMapper requestMapper;

    private final PasswordEncoder passwordEncoder;

    // --- Residential Complexes ---

    @Override
    public List<ResidentialComplexDto> listAllComplexes() {
        log.info("Listing all residential complexes");
        return complexRepo.findAll().stream().map(complexMapper::toDto).toList();
    }

    @Override
    public ResidentialComplexDto getComplex(UUID complexId) {
        log.info("Getting residential complex with id={}", complexId);
        var complex =
                complexRepo
                        .findById(complexId)
                        .orElseThrow(
                                () -> new NotFoundException("Complex not found: " + complexId));
        return complexMapper.toDto(complex);
    }

    @Override
    public ResidentialComplexDto saveComplex(ResidentialComplexCommandDto dto) {
        log.info("Saving new residential complex: {}", dto);
        var domain =
                ResidentialComplex.builder()
                        .name(dto.name())
                        .address(dto.address())
                        .city(dto.city())
                        .postalCode(dto.postalCode())
                        .build();
        var saved = complexRepo.save(domain);
        log.info("Saved residential complex with id={}", saved.getId());
        return complexMapper.toDto(saved);
    }

    @Override
    public ResidentialComplexDto updateComplex(UUID complexId, ResidentialComplexCommandDto dto) {
        log.info("Updating residential complex with id={}", complexId);
        var existing =
                complexRepo
                        .findById(complexId)
                        .orElseThrow(
                                () -> new NotFoundException("Complex not found: " + complexId));
        var updated =
                existing.toBuilder()
                        .name(dto.name())
                        .address(dto.address())
                        .city(dto.city())
                        .postalCode(dto.postalCode())
                        .build();
        var saved = complexRepo.save(updated);
        log.info("Updated residential complex with id={}", complexId);
        return complexMapper.toDto(saved);
    }

    @Override
    public DeleteResponseDto deleteComplex(UUID complexId) {
        log.info("Deleting residential complex with id={}", complexId);
        complexRepo.deleteById(complexId);
        log.info("Deleted residential complex with id={}", complexId);
        return new DeleteResponseDto("Residential complex deleted: " + complexId);
    }

    // --- Residents ---

    @Override
    public List<ResidentDto> listAllResidents() {
        log.info("Listing all residents");
        return residentRepo.findAll().stream().map(residentMapper::toDto).toList();
    }

    @Override
    public ResidentDto getResident(UUID residentId) {
        log.info("Getting resident with id={}", residentId);
        var r =
                residentRepo
                        .findById(residentId)
                        .orElseThrow(
                                () -> new NotFoundException("Resident not found: " + residentId));
        return residentMapper.toDto(r);
    }

    @Override
    public ResidentDto saveResident(ResidentCreateCommandDto dto) {
        log.info("Saving new resident: {}", dto);
        if (userRepo.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        var domain =
                Resident.builder()
                        .fullName(dto.fullName())
                        .email(dto.email())
                        .passwordHash(passwordEncoder.encode(dto.password()))
                        .unitNumber(dto.unitNumber())
                        .unitBlock(dto.unitBlock())
                        .residentialComplex(
                                complexRepo
                                        .findById(dto.residentialComplexId())
                                        .orElseThrow(
                                                () ->
                                                        new NotFoundException(
                                                                "Complex not found: "
                                                                        + dto
                                                                                .residentialComplexId())))
                        .build();
        var saved = residentRepo.save(domain);
        log.info("Saved resident with id={}", saved.getId());
        return residentMapper.toDto(saved);
    }

    @Override
    public ResidentDto updateResident(UUID residentId, ResidentUpdateCommandDto dto) {
        log.info("Updating resident with id={}", residentId);
        var existing =
                residentRepo
                        .findById(residentId)
                        .orElseThrow(
                                () -> new NotFoundException("Resident not found: " + residentId));
        // Email uniqueness check
        var userWithEmail = userRepo.findByEmail(dto.email());
        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(residentId)) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        var builder = existing.toBuilder()
                .fullName(dto.fullName())
                .email(dto.email())
                .unitNumber(dto.unitNumber())
                .unitBlock(dto.unitBlock())
                .residentialComplex(
                        complexRepo
                                .findById(dto.residentialComplexId())
                                .orElseThrow(
                                        () ->
                                                new NotFoundException(
                                                        "Complex not found: "
                                                                + dto.residentialComplexId())));
        if (dto.password() != null && !dto.password().isBlank()) {
            builder.passwordHash(dto.password());
        }
        var updated = builder.build();
        var saved = residentRepo.save(updated);
        log.info("Updated resident with id={}", residentId);
        return residentMapper.toDto(saved);
    }

    @Override
    public DeleteResponseDto deleteResident(UUID residentId) {
        log.info("Deleting resident with id={}", residentId);
        residentRepo.deleteById(residentId);
        log.info("Deleted resident with id={}", residentId);
        return new DeleteResponseDto("Resident deleted: " + residentId);
    }

    // --- Technicians ---

    @Override
    public List<TechnicianDto> listAllTechnicians() {
        log.info("Listing all technicians");
        return technicianRepo.findAll().stream().map(technicianMapper::toDto).toList();
    }

    @Override
    public TechnicianDto getTechnician(UUID technicianId) {
        log.info("Getting technician with id={}", technicianId);
        var t =
                technicianRepo
                        .findById(technicianId)
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                "Technician not found: " + technicianId));
        return technicianMapper.toDto(t);
    }

    @Override
    public TechnicianDto saveTechnician(TechnicianCreateCommandDto dto) {
        log.info("Saving new technician: {}", dto);
        if (userRepo.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        var domain =
                Technician.builder()
                        .fullName(dto.fullName())
                        .email(dto.email())
                        .passwordHash(passwordEncoder.encode(dto.password()))
                        .specializations(new HashSet<>(dto.specializations()))
                        .build();
        var saved = technicianRepo.save(domain);
        log.info("Saved technician with id={}", saved.getId());
        return technicianMapper.toDto(saved);
    }

    @Override
    public TechnicianDto updateTechnician(UUID technicianId, TechnicianUpdateCommandDto dto) {
        log.info("Updating technician with id={}", technicianId);
        var existing =
                technicianRepo
                        .findById(technicianId)
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                "Technician not found: " + technicianId));
        var userWithEmail = userRepo.findByEmail(dto.email());
        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(technicianId)) {
            throw new EmailAlreadyExistsException(dto.email());
        }
        var builder = existing.toBuilder()
                .id(existing.getId())
                .fullName(dto.fullName())
                .email(dto.email())
                .specializations(new HashSet<>(dto.specializations()));
        if (dto.password() != null && !dto.password().isBlank()) {
            builder.passwordHash(dto.password());
        }
        var updated = builder.build();
        var saved = technicianRepo.save(updated);
        log.info("Updated technician with id={}", technicianId);
        return technicianMapper.toDto(saved);
    }

    @Override
    public DeleteResponseDto deleteTechnician(UUID technicianId) {
        log.info("Deleting technician with id={}", technicianId);
        technicianRepo.deleteById(technicianId);
        log.info("Deleted technician with id={}", technicianId);
        return new DeleteResponseDto("Technician deleted: " + technicianId);
    }

    // --- Maintenance Requests ---

    @Override
    public List<MaintenanceRequestDto> listAllRequests() {
        log.info("Listing all maintenance requests");
        return requestRepo.findAll().stream().map(requestMapper::toDto).toList();
    }

    @Override
    public MaintenanceRequestDto getRequest(UUID requestId) {
        log.info("Getting maintenance request with id={}", requestId);
        var r =
                requestRepo
                        .findById(requestId)
                        .orElseThrow(
                                () -> new NotFoundException("Request not found: " + requestId));
        return requestMapper.toDto(r);
    }

    @Override
    public MaintenanceRequestDto assignTechnician(UUID requestId, UUID technicianId) {
        log.info("Assigning technicianId={} to requestId={}", technicianId, requestId);
        var existing =
                requestRepo
                        .findById(requestId)
                        .orElseThrow(
                                () -> new NotFoundException("Request not found: " + requestId));
        var tech =
                technicianRepo
                        .findById(technicianId)
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                "Technician not found: " + technicianId));
        var updated = existing.toBuilder().technician(tech).status(RequestStatus.PENDING).build();
        var saved = requestRepo.save(updated);
        log.info("Assigned technicianId={} to requestId={}", technicianId, requestId);
        return requestMapper.toDto(saved);
    }

    @Override
    public MaintenanceRequestDto createRequest(CreateMaintenanceRequestDto dto) {
        if (dto.residentId() == null) {
            throw new IllegalArgumentException("Resident ID must not be null");
        }
        var resident = residentRepo.findById(dto.residentId())
            .orElseThrow(() -> new NotFoundException("Resident not found: " + dto.residentId()));
        var req = MaintenanceRequest.builder()
                .resident(resident)
                .description(dto.description())
                .specialization(dto.specialization())
                .status(RequestStatus.PENDING)
                .createdAt(java.time.LocalDateTime.now())
                .scheduledAt(dto.scheduledAt())
                .build();
        var saved = requestRepo.save(req);
        return requestMapper.toDto(saved);
    }

    @Override
    public MaintenanceRequestDto updateRequest(UUID requestId, UpdateRequestDto dto) {
        var existing = requestRepo.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found: " + requestId));
        var updated = existing.toBuilder()
                .description(dto.description())
                .scheduledAt(dto.scheduledAt())
                .build();
        var saved = requestRepo.save(updated);
        return requestMapper.toDto(saved);
    }

    @Override
    public DeleteResponseDto deleteRequest(UUID requestId) {
        log.info("Deleting maintenance request with id={}", requestId);
        requestRepo.deleteById(requestId);
        log.info("Deleted maintenance request with id={}", requestId);
        return new DeleteResponseDto("Request deleted: " + requestId);
    }
}
