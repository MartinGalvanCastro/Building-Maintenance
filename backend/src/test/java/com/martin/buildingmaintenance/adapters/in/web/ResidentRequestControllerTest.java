package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.port.in.ResidentRequestService;
import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResidentRequestController.class)
@Import(SecurityConfig.class)
class ResidentRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ResidentRequestService svc;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private BlacklistedTokenAdapter tokenBlacklist;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    private static final String VALID_TOKEN = "valid-token";
    private static final UUID ADMIN_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID RESIDENT_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID REQUEST_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(1);
    private static final Specialization SPECIALIZATION = Specialization.ELECTRICAL;

    private static Arguments[] ROLES = new Arguments[] {
        arguments("ADMIN", ADMIN_ID),
        arguments("RESIDENT", RESIDENT_ID)
    };

    @Nested
    @DisplayName("GET /residents/{id}/requests")
    class GetResidentRequests {
        static Stream<Arguments> roles() { return Stream.of(ROLES); }
        @ParameterizedTest
        @MethodSource("roles")
        void user_can_list_their_requests(String role, UUID userId) throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, role, userId, VALID_TOKEN);
            mockMvc.perform(get("/residents/" + userId + "/requests").header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(svc).listMyRequests(userId);
        }
        @Test
        void list_requests_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests")
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(svc, never()).listMyRequests(any());
        }
        @Test
        void list_requests_missing_token_returns_403() throws Exception {
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests"))
                    .andExpect(status().isForbidden());
            verify(svc, never()).listMyRequests(any());
        }
        @Test
        void list_requests_non_resident_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", RESIDENT_ID, VALID_TOKEN);
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests")
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(svc, never()).listMyRequests(any());
        }
    }

    @Nested
    @DisplayName("GET /residents/{id}/requests/{requestId}")
    class GetResidentRequestById {
        static Stream<Arguments> roles() { return Stream.of(ROLES); }
        @ParameterizedTest
        @MethodSource("roles")
        void user_can_get_specific_request(String role, UUID userId) throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, role, userId, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto.class);
            when(svc.getMyRequest(userId, REQUEST_ID)).thenReturn(response);
            mockMvc.perform(get("/residents/" + userId + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(svc).getMyRequest(userId, REQUEST_ID);
        }
        @Test
        void get_request_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", RESIDENT_ID, VALID_TOKEN);
            when(svc.getMyRequest(RESIDENT_ID, REQUEST_ID)).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Request not found"));
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
        @Test
        void get_request_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(svc, never()).getMyRequest(any(), any());
        }
        @Test
        void get_request_missing_token_returns_403() throws Exception {
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID))
                    .andExpect(status().isForbidden());
            verify(svc, never()).getMyRequest(any(), any());
        }
        @Test
        void get_request_non_resident_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", RESIDENT_ID, VALID_TOKEN);
            mockMvc.perform(get("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(svc, never()).getMyRequest(any(), any());
        }
    }

    @Nested
    @DisplayName("POST /residents/{id}/requests")
    class CreateResidentRequest {
        static Stream<Arguments> roles() { return Stream.of(ROLES); }
        @ParameterizedTest
        @MethodSource("roles")
        void user_can_create_request(String role, UUID userId) throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, role, userId, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.CreateRequestDto(
                "Fix light", SPECIALIZATION, FUTURE_DATE
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto.class);
            when(svc.createRequest(eq(userId), any())).thenReturn(response);
            mockMvc.perform(post("/residents/" + userId + "/requests")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
            verify(svc).createRequest(eq(userId), any());
        }
        @Test
        void create_request_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", RESIDENT_ID, VALID_TOKEN);
            // Missing description and specialization
            String payload = "{" +
                    "\"scheduledAt\": \"" + FUTURE_DATE + "\"}";
            mockMvc.perform(post("/residents/" + RESIDENT_ID + "/requests")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
        @Test
        void create_request_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.CreateRequestDto(
                "Fix light", SPECIALIZATION, FUTURE_DATE
            );
            mockMvc.perform(post("/residents/" + RESIDENT_ID + "/requests")
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).createRequest(any(), any());
        }
        @Test
        void create_request_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.CreateRequestDto(
                "Fix light", SPECIALIZATION, FUTURE_DATE
            );
            mockMvc.perform(post("/residents/" + RESIDENT_ID + "/requests")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).createRequest(any(), any());
        }
        @Test
        void create_request_non_resident_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", RESIDENT_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.CreateRequestDto(
                "Fix light", SPECIALIZATION, FUTURE_DATE
            );
            mockMvc.perform(post("/residents/" + RESIDENT_ID + "/requests")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).createRequest(any(), any());
        }
    }

    @Nested
    @DisplayName("PATCH /residents/{id}/requests/{requestId}")
    class UpdateResidentRequest {
        static Stream<Arguments> roles() { return Stream.of(ROLES); }
        @ParameterizedTest
        @MethodSource("roles")
        void user_can_update_request(String role, UUID userId) throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, role, userId, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.UpdateRequestDto(
                "Fix outlet", FUTURE_DATE
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto.class);
            when(svc.updateMyRequest(eq(userId), eq(REQUEST_ID), any())).thenReturn(response);
            mockMvc.perform(patch("/residents/" + userId + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
            verify(svc).updateMyRequest(eq(userId), eq(REQUEST_ID), any());
        }
        @Test
        void update_request_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", RESIDENT_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.UpdateRequestDto(
                "Fix outlet", FUTURE_DATE
            );
            when(svc.updateMyRequest(eq(RESIDENT_ID), eq(REQUEST_ID), any())).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Request not found"));
            mockMvc.perform(patch("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }
        @Test
        void update_request_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", RESIDENT_ID, VALID_TOKEN);
            // Missing description
            String payload = "{" +
                    "\"scheduledAt\": \"" + FUTURE_DATE + "\"}";
            mockMvc.perform(patch("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
        @Test
        void update_request_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.UpdateRequestDto(
                "Fix outlet", FUTURE_DATE
            );
            mockMvc.perform(patch("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).updateMyRequest(any(), any(), any());
        }
        @Test
        void update_request_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.UpdateRequestDto(
                "Fix outlet", FUTURE_DATE
            );
            mockMvc.perform(patch("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).updateMyRequest(any(), any(), any());
        }
        @Test
        void update_request_non_resident_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", RESIDENT_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.UpdateRequestDto(
                "Fix outlet", FUTURE_DATE
            );
            mockMvc.perform(patch("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(svc, never()).updateMyRequest(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /residents/{id}/requests/{requestId}")
    class CancelResidentRequest {
        static Stream<Arguments> roles() { return Stream.of(ROLES); }
        @ParameterizedTest
        @MethodSource("roles")
        void user_can_cancel_request(String role, UUID userId) throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, role, userId, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.CancelResponseDto.class);
            when(svc.cancelMyRequest(userId, REQUEST_ID)).thenReturn(response);
            mockMvc.perform(delete("/residents/" + userId + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(svc).cancelMyRequest(userId, REQUEST_ID);
        }
        @Test
        void cancel_request_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", RESIDENT_ID, VALID_TOKEN);
            doThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Request not found")).when(svc).cancelMyRequest(RESIDENT_ID, REQUEST_ID);
            mockMvc.perform(delete("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
        @Test
        void cancel_request_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(delete("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(svc, never()).cancelMyRequest(any(), any());
        }
        @Test
        void cancel_request_missing_token_returns_403() throws Exception {
            mockMvc.perform(delete("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID))
                    .andExpect(status().isForbidden());
            verify(svc, never()).cancelMyRequest(any(), any());
        }
        @Test
        void cancel_request_non_resident_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", RESIDENT_ID, VALID_TOKEN);
            mockMvc.perform(delete("/residents/" + RESIDENT_ID + "/requests/" + REQUEST_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(svc, never()).cancelMyRequest(any(), any());
        }
    }
}
