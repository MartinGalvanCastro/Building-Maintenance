package com.martin.buildingmaintenance.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
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

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResidentManagementController.class)
@Import(SecurityConfig.class)
class ResidentManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AdminManagementService adminSvc;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private BlacklistedTokenAdapter tokenBlacklist;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String VALID_TOKEN = "valid-token";
    private static final UUID ADMIN_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID COMPLEX_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID RESIDENT_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");

    @Nested
    @DisplayName("GET /residents")
    class GetResidents {
        @Test
        @DisplayName("Admin can access resident management")
        void admin_can_access_resident_management() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(get("/residents").header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).listAllResidents();
        }
    }

    @Nested
    @DisplayName("POST /residents")
    class CreateResident {
        @Test
        @DisplayName("Admin can create resident")
        void admin_can_create_resident() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentCreateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.ResidentDto.class);
            when(adminSvc.saveResident(any())).thenReturn(response);
            mockMvc.perform(post("/residents")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
            verify(adminSvc).saveResident(any());
        }

        @Test
        @DisplayName("Create resident missing fields returns 400")
        void create_resident_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing fullName and email
            String payload = "{" +
                    "\"password\": \"password123\"," +
                    "\"unitNumber\": \"101\"," +
                    "\"unitBlock\": \"A\"," +
                    "\"residentialComplexId\": \"" + COMPLEX_ID + "\"}";
            mockMvc.perform(post("/residents")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Create resident invalid token returns 403")
        void create_resident_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentCreateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(post("/residents")
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveResident(any());
        }

        @Test
        @DisplayName("Create resident missing token returns 403")
        void create_resident_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentCreateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(post("/residents")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveResident(any());
        }

        @Test
        @DisplayName("Create resident non-admin returns 403")
        void create_resident_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentCreateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(post("/residents")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveResident(any());
        }
    }

    @Nested
    @DisplayName("PUT /residents/{id}")
    class UpdateResident {
        @Test
        @DisplayName("Admin can update resident")
        void admin_can_update_resident() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.ResidentDto.class);
            when(adminSvc.updateResident(eq(RESIDENT_ID), any())).thenReturn(response);
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
            verify(adminSvc).updateResident(eq(RESIDENT_ID), any());
        }

        @Test
        @DisplayName("Update resident missing fields returns 400")
        void update_resident_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing unitNumber and unitBlock
            String payload = "{" +
                    "\"fullName\": \"John Doe\"," +
                    "\"email\": \"john@example.com\"}";
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Update resident invalid token returns 403")
        void update_resident_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateResident(any(), any());
        }

        @Test
        @DisplayName("Update resident missing token returns 403")
        void update_resident_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateResident(any(), any());
        }

        @Test
        @DisplayName("Update resident non-admin returns 403")
        void update_resident_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateResident(any(), any());
        }

        @Test
        @DisplayName("Update nonexistent resident returns 404")
        void update_nonexistent_resident_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.ResidentUpdateCommandDto(
                "John Doe", "john@example.com", "password123", "101", "A", COMPLEX_ID
            );
            when(adminSvc.updateResident(eq(RESIDENT_ID), any())).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Resident not found"));
            mockMvc.perform(put("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /residents/{id}")
    class GetResidentById {
        @Test
        @DisplayName("Admin can get resident by id")
        void admin_can_get_resident_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.ResidentDto.class);
            when(adminSvc.getResident(RESIDENT_ID)).thenReturn(response);
            mockMvc.perform(get("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).getResident(RESIDENT_ID);
        }

        @Test
        @DisplayName("Get resident invalid token returns 403")
        void get_resident_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(get("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getResident(any());
        }

        @Test
        @DisplayName("Get resident missing token returns 403")
        void get_resident_missing_token_returns_403() throws Exception {
            mockMvc.perform(get("/residents/" + RESIDENT_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getResident(any());
        }

        @Test
        @DisplayName("Get resident non-admin returns 403")
        void get_resident_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(get("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getResident(any());
        }

        @Test
        @DisplayName("Get nonexistent resident returns 404")
        void get_nonexistent_resident_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            when(adminSvc.getResident(RESIDENT_ID)).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Resident not found"));
            mockMvc.perform(get("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /residents/{id}")
    class DeleteResident {
        @Test
        @DisplayName("Admin can delete resident")
        void admin_can_delete_resident() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.DeleteResponseDto.class);
            when(adminSvc.deleteResident(RESIDENT_ID)).thenReturn(response);
            mockMvc.perform(delete("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).deleteResident(RESIDENT_ID);
        }

        @Test
        @DisplayName("Delete resident invalid token returns 403")
        void delete_resident_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(delete("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteResident(any());
        }

        @Test
        @DisplayName("Delete resident missing token returns 403")
        void delete_resident_missing_token_returns_403() throws Exception {
            mockMvc.perform(delete("/residents/" + RESIDENT_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteResident(any());
        }

        @Test
        @DisplayName("Delete resident non-admin returns 403")
        void delete_resident_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(delete("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteResident(any());
        }

        @Test
        @DisplayName("Delete nonexistent resident returns 404")
        void delete_nonexistent_resident_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            doThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Resident not found")).when(adminSvc).deleteResident(RESIDENT_ID);
            mockMvc.perform(delete("/residents/" + RESIDENT_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
    }
}
