package com.martin.buildingmaintenance.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.application.dto.CredentialsDto;
import com.martin.buildingmaintenance.application.dto.LogInResultDto;
import com.martin.buildingmaintenance.application.dto.LogOutResponseDto;
import com.martin.buildingmaintenance.application.exception.BadCredentialsException;
import com.martin.buildingmaintenance.application.port.out.TokenBlacklistRepository;
import com.martin.buildingmaintenance.application.port.out.UserRepository;
import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import com.martin.buildingmaintenance.security.SudoAdminProperties;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock UserRepository userRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock TokenBlacklistRepository tokenBlacklistRepository;
    @Mock SudoAdminProperties sudoAdminProperties;
    @InjectMocks AuthServiceImpl service;

    @Test
    void authenticate_success() {
        CredentialsDto credentials = mock(CredentialsDto.class);
        User user = mock(User.class);
        when(credentials.email()).thenReturn("test@example.com");
        when(credentials.password()).thenReturn("pass");
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(user.getPasswordHash()).thenReturn("hash");
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn("token");
        LogInResultDto result = service.authenticate(credentials);
        assertEquals("token", result.token());
    }

    @Test
    void authenticate_userNotFound_throws() {
        CredentialsDto credentials = mock(CredentialsDto.class);
        when(credentials.email()).thenReturn("test@example.com");
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());
        assertThrows(BadCredentialsException.class, () -> service.authenticate(credentials));
    }

    @Test
    void authenticate_passwordMismatch_throws() {
        CredentialsDto credentials = mock(CredentialsDto.class);
        User user = mock(User.class);
        when(credentials.email()).thenReturn("test@example.com");
        when(credentials.password()).thenReturn("pass");
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(user.getPasswordHash()).thenReturn("hash");
        when(passwordEncoder.matches("pass", "hash")).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> service.authenticate(credentials));
    }

    @Test
    void logout_success() {
        String token = "token";
        LogOutResponseDto result = service.logout(token);
        assertEquals("Successfully logged out", result.message());
        verify(tokenBlacklistRepository).revoke(token);
    }

    @Test
    void authenticate_sudoAdmin_success() {
        CredentialsDto credentials = mock(CredentialsDto.class);
        when(credentials.email()).thenReturn("admin@sudo.com");
        when(credentials.password()).thenReturn("adminpass");
        when(sudoAdminProperties.getUsername()).thenReturn("admin@sudo.com");
        when(sudoAdminProperties.getPassword()).thenReturn("adminpass");
        when(jwtTokenProvider.generateToken(any(UUID.class), anyString())).thenReturn("sudotoken");
        LogInResultDto result = service.authenticate(credentials);
        assertEquals("sudotoken", result.token());
    }
}
