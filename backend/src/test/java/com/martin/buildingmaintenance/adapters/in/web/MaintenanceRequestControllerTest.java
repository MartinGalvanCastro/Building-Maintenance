package com.martin.buildingmaintenance.adapters.in.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.dto.CreateMaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.CreateRequestDto;
import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.dto.UpdateRequestDto;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MaintenanceRequestController.class)
@Import(SecurityConfig.class)
class MaintenanceRequestControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private AdminManagementService adminSvc;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private BlacklistedTokenAdapter tokenBlacklist;

    private static final String VALID_TOKEN = "valid-token";
    private static final UUID ADMIN_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID REQUEST_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID TECHNICIAN_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Nested
    @DisplayName("GET /maintenance-requests")
    class ListRequests {
        @Test
        void admin_can_list_all_requests() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            when(adminSvc.listAllRequests()).thenReturn(List.of());
            mockMvc.perform(
                            get("/maintenance-requests")
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).listAllRequests();
        }

        @Test
        void non_admin_cannot_list_all_requests() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            get("/maintenance-requests")
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllRequests();
        }

        @Test
        void unauthenticated_cannot_list_all_requests() throws Exception {
            mockMvc.perform(get("/maintenance-requests")).andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllRequests();
        }

        @Test
        void invalid_token_cannot_list_all_requests() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            get("/maintenance-requests")
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllRequests();
        }
    }

    @Nested
    @DisplayName("GET /maintenance-requests/{id}")
    class GetRequest {
        @Test
        void admin_can_get_request_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
            when(adminSvc.getRequest(REQUEST_ID)).thenReturn(dto);
            mockMvc.perform(
                            get("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).getRequest(REQUEST_ID);
        }

        @Test
        void non_admin_cannot_get_request_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            get("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getRequest(any());
        }

        @Test
        void unauthenticated_cannot_get_request_by_id() throws Exception {
            mockMvc.perform(get("/maintenance-requests/" + REQUEST_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getRequest(any());
        }

        @Test
        void invalid_token_cannot_get_request_by_id() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            get("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getRequest(any());
        }
    }

    @Nested
    @DisplayName("PATCH /maintenance-requests/{id}/technician/{technicianId}")
    class AssignTechnician {
        @Test
        void admin_can_assign_technician() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
            when(adminSvc.assignTechnician(REQUEST_ID, TECHNICIAN_ID)).thenReturn(dto);
            mockMvc.perform(
                            patch(
                                            "/maintenance-requests/"
                                                    + REQUEST_ID
                                                    + "/technician/"
                                                    + TECHNICIAN_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).assignTechnician(REQUEST_ID, TECHNICIAN_ID);
        }

        @Test
        void non_admin_cannot_assign_technician() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            patch(
                                            "/maintenance-requests/"
                                                    + REQUEST_ID
                                                    + "/technician/"
                                                    + TECHNICIAN_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).assignTechnician(any(), any());
        }

        @Test
        void unauthenticated_cannot_assign_technician() throws Exception {
            mockMvc.perform(
                            patch(
                                    "/maintenance-requests/"
                                            + REQUEST_ID
                                            + "/technician/"
                                            + TECHNICIAN_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).assignTechnician(any(), any());
        }

        @Test
        void invalid_token_cannot_assign_technician() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            patch(
                                            "/maintenance-requests/"
                                                    + REQUEST_ID
                                                    + "/technician/"
                                                    + TECHNICIAN_ID)
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).assignTechnician(any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /maintenance-requests/{id}")
    class DeleteRequest {
        @Test
        void admin_can_delete_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            DeleteResponseDto dto = mock(DeleteResponseDto.class);
            when(adminSvc.deleteRequest(REQUEST_ID)).thenReturn(dto);
            mockMvc.perform(
                            delete("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).deleteRequest(REQUEST_ID);
        }

        @Test
        void non_admin_cannot_delete_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            delete("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteRequest(any());
        }

        @Test
        void unauthenticated_cannot_delete_request() throws Exception {
            mockMvc.perform(delete("/maintenance-requests/" + REQUEST_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteRequest(any());
        }

        @Test
        void invalid_token_cannot_delete_request() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            delete("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteRequest(any());
        }
    }

    @Nested
    @DisplayName("POST /maintenance-requests")
    class CreateRequest {
        @Test
        void admin_can_create_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            CreateRequestDto dto =
                    new CreateRequestDto(
                            "desc", Specialization.PLUMBING, LocalDateTime.now().plusDays(1));
            MaintenanceRequestDto out = mock(MaintenanceRequestDto.class);
            when(adminSvc.createRequest(any())).thenReturn(out);
            mockMvc.perform(
                            post("/maintenance-requests")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isOk());
            verify(adminSvc).createRequest(any());
        }

        @Test
        void non_admin_cannot_create_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            CreateRequestDto dto =
                    new CreateRequestDto(
                            "desc", Specialization.PLUMBING, LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            post("/maintenance-requests")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).createRequest(any());
        }

        @Test
        void unauthenticated_cannot_create_request() throws Exception {
            CreateRequestDto dto =
                    new CreateRequestDto(
                            "desc", Specialization.PLUMBING, LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            post("/maintenance-requests")
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).createRequest(any());
        }

        @Test
        void invalid_token_cannot_create_request() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            CreateRequestDto dto =
                    new CreateRequestDto(
                            "desc", Specialization.PLUMBING, LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            post("/maintenance-requests")
                                    .header("Authorization", "Bearer invalid-token")
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).createRequest(any());
        }

        @Test
        void invalid_payload_returns_bad_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing required fields (all null)
            CreateMaintenanceRequestDto dto =
                    new CreateMaintenanceRequestDto(null, null, null, null);
            mockMvc.perform(
                            post("/maintenance-requests")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
            verify(adminSvc, never()).createRequest(any());
        }
    }

    @Nested
    @DisplayName("PUT /maintenance-requests/{id}")
    class UpdateRequest {
        @Test
        void admin_can_update_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            UpdateRequestDto dto = new UpdateRequestDto("desc", LocalDateTime.now().plusDays(1));
            MaintenanceRequestDto out = mock(MaintenanceRequestDto.class);
            when(adminSvc.updateRequest(eq(REQUEST_ID), any())).thenReturn(out);
            mockMvc.perform(
                            put("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isOk());
            verify(adminSvc).updateRequest(eq(REQUEST_ID), any());
        }

        @Test
        void non_admin_cannot_update_request() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            UpdateRequestDto dto = new UpdateRequestDto("desc", LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            put("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateRequest(any(), any());
        }

        @Test
        void unauthenticated_cannot_update_request() throws Exception {
            UpdateRequestDto dto = new UpdateRequestDto("desc", LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            put("/maintenance-requests/" + REQUEST_ID)
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateRequest(any(), any());
        }

        @Test
        void invalid_token_cannot_update_request() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            UpdateRequestDto dto = new UpdateRequestDto("desc", LocalDateTime.now().plusDays(1));
            mockMvc.perform(
                            put("/maintenance-requests/" + REQUEST_ID)
                                    .header("Authorization", "Bearer invalid-token")
                                    .contentType("application/json")
                                    .content(new ObjectMapper().writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateRequest(any(), any());
        }
    }
}
