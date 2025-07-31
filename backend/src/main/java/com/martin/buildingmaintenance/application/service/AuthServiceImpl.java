package com.martin.buildingmaintenance.application.service;

import com.martin.buildingmaintenance.application.dto.CredentialsDto;
import com.martin.buildingmaintenance.application.dto.LogInResultDto;
import com.martin.buildingmaintenance.application.dto.LogOutResponseDto;
import com.martin.buildingmaintenance.application.exception.BadCredentialsException;
import com.martin.buildingmaintenance.application.port.in.AuthService;
import com.martin.buildingmaintenance.application.port.out.TokenBlacklistRepository;
import com.martin.buildingmaintenance.application.port.out.UserRepository;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import com.martin.buildingmaintenance.security.SudoAdminProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final SudoAdminProperties sudoProps;


    @Override
    public LogInResultDto authenticate(CredentialsDto credentialsDto) {

        if (credentialsDto.email().equals(sudoProps.getUsername()) &&
                credentialsDto.password().equals(sudoProps.getPassword())) {
         
            String token = jwtTokenProvider.generateToken(
                    UUID.nameUUIDFromBytes(sudoProps.getUsername().getBytes()),
                    "ADMIN"
            );
            return new LogInResultDto(token);
        }

        var user = userRepo.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(credentialsDto.password(), user.getPasswordHash())){
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
        return new LogInResultDto(token);
    }

    @Override
    public LogOutResponseDto logout(String token) {
        tokenBlacklistRepository.revoke(token);
        return new LogOutResponseDto("Successfully logged out");
    }
}
