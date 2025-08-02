package com.martin.buildingmaintenance.application.service;

import com.martin.buildingmaintenance.application.dto.UserInfoDto;
import com.martin.buildingmaintenance.application.exception.AccessDeniedException;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.in.UserInfoService;
import com.martin.buildingmaintenance.application.port.out.MaintenanceRequestRepository;
import com.martin.buildingmaintenance.application.port.out.ResidentRepository;
import com.martin.buildingmaintenance.application.port.out.TechnicianRepository;
import com.martin.buildingmaintenance.application.port.out.UserRepository;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentialComplexMapper;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final JwtTokenProvider jwtTokenProvider;
    private final ResidentRepository residentRepository;
    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final MaintenanceRequestMapper maintenanceRequestMapper;
    private final ResidentialComplexMapper residentialComplexMapper;

    @Override
    public UserInfoDto getCurrentUserInfo(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        Jws<Claims> claimsJws = jwtTokenProvider.validateToken(token);
        Claims claims = claimsJws.getBody();
        String userId = claims.getSubject();
        String role = (String) claims.get("role");

        if ("RESIDENT".equalsIgnoreCase(role)) {
            var resident = residentRepository.findById(UUID.fromString(userId));
            return resident.map(
                            r ->
                                    new UserInfoDto(
                                            r.getId().toString(),
                                            r.getFullName(),
                                            r.getEmail(),
                                            r.getRole(),
                                            residentialComplexMapper.toDto(
                                                    r.getResidentialComplex()),
                                            maintenanceRequestRepository
                                                    .findByResidentId(r.getId())
                                                    .stream()
                                                    .map(maintenanceRequestMapper::toSummary)
                                                    .toList()))
                    .orElseThrow(() -> new NotFoundException("Resident not found"));
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            var admin = userRepository.findById(UUID.fromString(userId));
            return admin.map(
                            a ->
                                    new UserInfoDto(
                                            a.getId().toString(),
                                            a.getFullName(),
                                            a.getEmail(),
                                            a.getRole(),
                                            null,
                                            null))
                    .orElseThrow(() -> new NotFoundException("Admin not found"));
        } else if ("TECHNICIAN".equalsIgnoreCase(role)) {
            var tech = technicianRepository.findById(UUID.fromString(userId));
            return tech.map(
                            t ->
                                    new UserInfoDto(
                                            t.getId().toString(),
                                            t.getFullName(),
                                            t.getEmail(),
                                            t.getRole(),
                                            null,
                                            maintenanceRequestRepository
                                                    .findByAssignedTechnicianId(t.getId())
                                                    .stream()
                                                    .map(maintenanceRequestMapper::toSummary)
                                                    .toList()))
                    .orElseThrow(() -> new NotFoundException("Technician not found"));
        } else {
            throw new AccessDeniedException("Unknown role: " + role);
        }
    }
}
