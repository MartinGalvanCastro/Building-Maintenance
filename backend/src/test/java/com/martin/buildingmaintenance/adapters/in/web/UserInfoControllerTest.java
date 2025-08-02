package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.UserInfoDto;
import com.martin.buildingmaintenance.application.port.in.UserInfoService;
import com.martin.buildingmaintenance.domain.model.Role;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.martin.buildingmaintenance.application.exception.AccessDeniedException;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;

@WebMvcTest(controllers = UserInfoController.class)
@Import(SecurityConfig.class)
class UserInfoControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private UserInfoService userInfoService;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private BlacklistedTokenAdapter tokenBlacklist;

    @Nested
    @DisplayName("GET /me")
    class GetMe {
        @Test
        void getMe_success() throws Exception {
            UserInfoDto dto = new UserInfoDto("id", "name", "email", Role.RESIDENT, null, Collections.emptyList());
            UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
            String token = "valid";
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", userId, token);
            when(userInfoService.getCurrentUserInfo(any())).thenReturn(dto);
            mockMvc.perform(get("/me").header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        @Test
        void getMe_unauthorized() throws Exception {
            when(userInfoService.getCurrentUserInfo(any())).thenThrow(new AccessDeniedException("Unauthorized"));
            mockMvc.perform(get("/me")).andExpect(status().isForbidden());
        }

        @Test
        void getMe_notFound() throws Exception {
            UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
            String token = "valid";
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", userId, token);
            when(userInfoService.getCurrentUserInfo(any())).thenThrow(new NotFoundException("Not found"));
            mockMvc.perform(get("/me").header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }
    }
}
