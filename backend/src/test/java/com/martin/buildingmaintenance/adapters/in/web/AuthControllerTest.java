package com.martin.buildingmaintenance.adapters.in.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.dto.CredentialsDto;
import com.martin.buildingmaintenance.application.dto.LogInResultDto;
import com.martin.buildingmaintenance.application.dto.LogOutResponseDto;
import com.martin.buildingmaintenance.application.port.in.AuthService;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private AuthService authService;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean BlacklistedTokenAdapter tokenBlacklist;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("POST /auth/login")
    class LoginTests {
        @Test
        void login_success() throws Exception {
            LogInResultDto result = new LogInResultDto("token");
            when(authService.authenticate(any())).thenReturn(result);
            CredentialsDto credentials = new CredentialsDto("test@example.com", "pass");
            String json = objectMapper.writeValueAsString(credentials);
            mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token"));
            verify(authService).authenticate(any(CredentialsDto.class));
        }

        @Test
        void login_invalid_returns400() throws Exception {
            CredentialsDto credentials = new CredentialsDto("", "");
            String json = objectMapper.writeValueAsString(credentials);
            mockMvc.perform(
                            post("/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                    .andExpect(status().isBadRequest());
            verify(authService, never()).authenticate(any());
        }
    }

    @Nested
    @DisplayName("POST /auth/logout")
    class LogoutTests {
        @Test
        void logout_missingAuthorizationHeader_returns403() throws Exception {
            mockMvc.perform(post("/auth/logout")).andExpect(status().isForbidden());
            verify(authService, never()).logout(any());
        }

        @Test
        void logout_success() throws Exception {
            Claims claims = mock(Claims.class);
            when(claims.get("role")).thenReturn("USER");
            when(claims.getSubject()).thenReturn(UUID.randomUUID().toString());
            Jws<Claims> jws = mock(Jws.class);
            when(jws.getBody()).thenReturn(claims);
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenAnswer(
                            invocation -> {
                                String token = invocation.getArgument(0);
                                if ("valid-token".equals(token)) return jws;
                                throw new io.jsonwebtoken.JwtException("Invalid token");
                            });
            when(tokenBlacklist.isRevoked(anyString())).thenReturn(false);
            when(authService.logout(anyString())).thenReturn(new LogOutResponseDto("Logged out"));
            mockMvc.perform(post("/auth/logout").header("Authorization", "Bearer valid-token"))
                    .andExpect(status().isOk());
            verify(authService).logout("valid-token");
        }

        @Test
        void logout_success_noBearerPrefix() throws Exception {
            mockMvc.perform(post("/auth/logout").header("Authorization", "token"))
                    .andExpect(status().isForbidden());
            verify(authService, never()).logout(any());
        }

        @Test
        void logout_with_expired_token_should_return_unauthorized() throws Exception {
            Claims claims = mock(Claims.class);
            when(claims.get("role")).thenReturn("USER");
            when(claims.getSubject()).thenReturn(UUID.randomUUID().toString());
            Jws<Claims> jws = mock(Jws.class);
            when(jws.getBody()).thenReturn(claims);
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenAnswer(
                            invocation -> {
                                String token = invocation.getArgument(0);
                                if ("expired-token".equals(token)) return jws;
                                throw new io.jsonwebtoken.JwtException("Invalid token");
                            });
            when(tokenBlacklist.isRevoked("expired-token")).thenReturn(true);
            mockMvc.perform(post("/auth/logout").header("Authorization", "Bearer expired-token"))
                    .andExpect(status().isForbidden());
            verify(authService, never()).logout(any());
        }
    }
}
