package com.martin.buildingmaintenance.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TechnicianManagementController.class)
@Import(SecurityConfig.class)
class TechnicianManagementControllerTest {
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
    private static final UUID TECHNICIAN_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final Set<Specialization> SPECIALIZATIONS = Set.of(Specialization.ELECTRICAL);

    @Nested
    @DisplayName("GET /technicians")
    class GetTechnicians {
        @Test
        void admin_can_access_technician_management() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(get("/technicians").header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).listAllTechnicians();
        }
        @Test
        void list_technicians_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(get("/technicians")
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllTechnicians();
        }
        @Test
        void list_technicians_missing_token_returns_403() throws Exception {
            mockMvc.perform(get("/technicians"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllTechnicians();
        }
        @Test
        void list_technicians_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(get("/technicians")
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllTechnicians();
        }
    }

    @Nested
    @DisplayName("GET /technicians/{id}")
    class GetTechnicianById {
        @Test
        void admin_can_get_technician_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.TechnicianDto.class);
            when(adminSvc.getTechnician(TECHNICIAN_ID)).thenReturn(response);
            mockMvc.perform(get("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).getTechnician(TECHNICIAN_ID);
        }
        @Test
        void get_technician_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            when(adminSvc.getTechnician(TECHNICIAN_ID)).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Technician not found"));
            mockMvc.perform(get("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
        @Test
        void get_technician_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(get("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getTechnician(any());
        }
        @Test
        void get_technician_missing_token_returns_403() throws Exception {
            mockMvc.perform(get("/technicians/" + TECHNICIAN_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getTechnician(any());
        }
        @Test
        void get_technician_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(get("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getTechnician(any());
        }
    }

    @Nested
    @DisplayName("POST /technicians")
    class CreateTechnician {
        @Test
        void admin_can_create_technician() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianCreateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.TechnicianDto.class);
            when(adminSvc.saveTechnician(any())).thenReturn(response);
            mockMvc.perform(post("/technicians")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
            verify(adminSvc).saveTechnician(any());
        }
        @Test
        void create_technician_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing fullName and specializations
            String payload = "{" +
                    "\"email\": \"jane@example.com\"," +
                    "\"password\": \"password123\"}";
            mockMvc.perform(post("/technicians")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
        @Test
        void create_technician_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianCreateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(post("/technicians")
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveTechnician(any());
        }
        @Test
        void create_technician_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianCreateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(post("/technicians")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveTechnician(any());
        }
        @Test
        void create_technician_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianCreateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(post("/technicians")
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveTechnician(any());
        }
    }

    @Nested
    @DisplayName("PUT /technicians/{id}")
    class UpdateTechnician {
        @Test
        void admin_can_update_technician() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            var response = mock(com.martin.buildingmaintenance.application.dto.TechnicianDto.class);
            when(adminSvc.updateTechnician(eq(TECHNICIAN_ID), any())).thenReturn(response);
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
            verify(adminSvc).updateTechnician(eq(TECHNICIAN_ID), any());
        }
        @Test
        void update_technician_missing_fields_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing email and specializations
            String payload = "{" +
                    "\"fullName\": \"Jane Doe\"}";
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
        @Test
        void update_technician_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            when(adminSvc.updateTechnician(eq(TECHNICIAN_ID), any())).thenThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Technician not found"));
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }
        @Test
        void update_technician_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer invalid-token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateTechnician(any(), any());
        }
        @Test
        void update_technician_missing_token_returns_403() throws Exception {
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateTechnician(any(), any());
        }
        @Test
        void update_technician_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", ADMIN_ID, VALID_TOKEN);
            var dto = new com.martin.buildingmaintenance.application.dto.TechnicianUpdateCommandDto(
                "Jane Doe", "jane@example.com", "password123", SPECIALIZATIONS
            );
            mockMvc.perform(put("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateTechnician(any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /technicians/{id}")
    class DeleteTechnician {
        @Test
        void admin_can_delete_technician() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            var response = mock(com.martin.buildingmaintenance.application.dto.DeleteResponseDto.class);
            when(adminSvc.deleteTechnician(TECHNICIAN_ID)).thenReturn(response);
            mockMvc.perform(delete("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).deleteTechnician(TECHNICIAN_ID);
        }
        @Test
        void delete_technician_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            doThrow(new com.martin.buildingmaintenance.application.exception.NotFoundException("Technician not found")).when(adminSvc).deleteTechnician(TECHNICIAN_ID);
            mockMvc.perform(delete("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
        @Test
        void delete_technician_invalid_token_returns_403() throws Exception {
            when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(delete("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteTechnician(any());
        }
        @Test
        void delete_technician_missing_token_returns_403() throws Exception {
            mockMvc.perform(delete("/technicians/" + TECHNICIAN_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteTechnician(any());
        }
        @Test
        void delete_technician_non_admin_returns_403() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(delete("/technicians/" + TECHNICIAN_ID)
                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteTechnician(any());
        }
    }
}
