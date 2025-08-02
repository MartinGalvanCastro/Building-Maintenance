package com.martin.buildingmaintenance.adapters.in.web;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.dto.DeleteResponseDto;
import com.martin.buildingmaintenance.application.dto.ResidentialComplexCommandDto;
import com.martin.buildingmaintenance.application.dto.ResidentialComplexDto;
import com.martin.buildingmaintenance.application.exception.NotFoundException;
import com.martin.buildingmaintenance.application.port.in.AdminManagementService;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import java.util.List;
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

@WebMvcTest(controllers = ResidentialComplexController.class)
@Import(SecurityConfig.class)
class ResidentialComplexControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AdminManagementService adminSvc;
    @MockitoBean private JwtTokenProvider jwtTokenProvider;
    @MockitoBean private BlacklistedTokenAdapter tokenBlacklist;

    private static final String VALID_TOKEN = "valid-token";
    private static final UUID ADMIN_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID COMPLEX_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Nested
    @DisplayName("GET /residential-complexes")
    class GetComplexes {
        @Test
        @DisplayName("Admin can list all complexes")
        void admin_can_list_all_complexes() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            when(adminSvc.listAllComplexes()).thenReturn(List.of());
            mockMvc.perform(
                            get("/residential-complexes")
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).listAllComplexes();
        }

        @Test
        @DisplayName("Non-admin cannot list all complexes")
        void non_admin_cannot_list_all_complexes() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            get("/residential-complexes")
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllComplexes();
        }

        @Test
        @DisplayName("Unauthenticated cannot list all complexes")
        void unauthenticated_cannot_list_all_complexes() throws Exception {
            mockMvc.perform(get("/residential-complexes")).andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllComplexes();
        }

        @Test
        @DisplayName("Invalid token cannot list all complexes")
        void invalid_token_cannot_list_all_complexes() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            get("/residential-complexes")
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).listAllComplexes();
        }
    }

    @Nested
    @DisplayName("GET /residential-complexes/{id}")
    class GetComplexById {
        @Test
        @DisplayName("Admin can get complex by id")
        void admin_can_get_complex_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexDto dto = mock(ResidentialComplexDto.class);
            when(adminSvc.getComplex(COMPLEX_ID)).thenReturn(dto);
            mockMvc.perform(
                            get("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).getComplex(COMPLEX_ID);
        }

        @Test
        @DisplayName("Non-admin cannot get complex by id")
        void non_admin_cannot_get_complex_by_id() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            get("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getComplex(any());
        }

        @Test
        @DisplayName("Unauthenticated cannot get complex by id")
        void unauthenticated_cannot_get_complex_by_id() throws Exception {
            mockMvc.perform(get("/residential-complexes/" + COMPLEX_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getComplex(any());
        }

        @Test
        @DisplayName("Invalid token cannot get complex by id")
        void invalid_token_cannot_get_complex_by_id() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            get("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).getComplex(any());
        }
    }

    @Nested
    @DisplayName("POST /residential-complexes")
    class CreateComplex {
        @Test
        @DisplayName("Admin can create complex")
        void admin_can_create_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            ResidentialComplexDto dto = mock(ResidentialComplexDto.class);
            when(adminSvc.saveComplex(any())).thenReturn(dto);
            mockMvc.perform(
                            post("/residential-complexes")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isCreated());
            verify(adminSvc).saveComplex(any());
        }

        @Test
        @DisplayName("Non-admin cannot create complex")
        void non_admin_cannot_create_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            mockMvc.perform(
                            post("/residential-complexes")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveComplex(any());
        }

        @Test
        @DisplayName("Unauthenticated cannot create complex")
        void unauthenticated_cannot_create_complex() throws Exception {
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            mockMvc.perform(
                            post("/residential-complexes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveComplex(any());
        }

        @Test
        @DisplayName("Invalid token cannot create complex")
        void invalid_token_cannot_create_complex() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            post("/residential-complexes")
                                    .header("Authorization", "Bearer invalid-token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).saveComplex(any());
        }

        @Test
        @DisplayName("Create complex missing attributes returns 400")
        void create_complex_missing_attributes_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing name and city
            String payload = "{" + "\"address\": \"123 Main St\"," + "\"postalCode\": \"12345\"}";
            mockMvc.perform(
                            post("/residential-complexes")
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /residential-complexes/{id}")
    class UpdateComplex {
        @Test
        @DisplayName("Admin can update complex")
        void admin_can_update_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            ResidentialComplexDto dto = mock(ResidentialComplexDto.class);
            when(adminSvc.updateComplex(eq(COMPLEX_ID), any())).thenReturn(dto);
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isOk());
            verify(adminSvc).updateComplex(eq(COMPLEX_ID), any());
        }

        @Test
        @DisplayName("Non-admin cannot update complex")
        void non_admin_cannot_update_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateComplex(any(), any());
        }

        @Test
        @DisplayName("Unauthenticated cannot update complex")
        void unauthenticated_cannot_update_complex() throws Exception {
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateComplex(any(), any());
        }

        @Test
        @DisplayName("Invalid token cannot update complex")
        void invalid_token_cannot_update_complex() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer invalid-token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{}"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).updateComplex(any(), any());
        }

        @Test
        @DisplayName("Update complex not found returns 404")
        void update_complex_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            ResidentialComplexCommandDto command =
                    new ResidentialComplexCommandDto(
                            "Test Complex", "123 Main St", "Testville", "12345");
            when(adminSvc.updateComplex(eq(COMPLEX_ID), any()))
                    .thenThrow(new NotFoundException("Complex not found"));
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Update complex missing attributes returns 400")
        void update_complex_missing_attributes_returns_400() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            // Missing address and postalCode
            String payload = "{" + "\"name\": \"Test Complex\"," + "\"city\": \"Testville\"}";
            mockMvc.perform(
                            put("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(payload))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /residential-complexes/{id}")
    class DeleteComplex {
        @Test
        @DisplayName("Admin can delete complex")
        void admin_can_delete_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            DeleteResponseDto dto = mock(DeleteResponseDto.class);
            when(adminSvc.deleteComplex(COMPLEX_ID)).thenReturn(dto);
            mockMvc.perform(
                            delete("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isOk());
            verify(adminSvc).deleteComplex(COMPLEX_ID);
        }

        @Test
        @DisplayName("Non-admin cannot delete complex")
        void non_admin_cannot_delete_complex() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "RESIDENT", ADMIN_ID, VALID_TOKEN);
            mockMvc.perform(
                            delete("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteComplex(any());
        }

        @Test
        @DisplayName("Unauthenticated cannot delete complex")
        void unauthenticated_cannot_delete_complex() throws Exception {
            mockMvc.perform(delete("/residential-complexes/" + COMPLEX_ID))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteComplex(any());
        }

        @Test
        @DisplayName("Invalid token cannot delete complex")
        void invalid_token_cannot_delete_complex() throws Exception {
            when(jwtTokenProvider.validateToken(anyString()))
                    .thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
            mockMvc.perform(
                            delete("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer invalid-token"))
                    .andExpect(status().isForbidden());
            verify(adminSvc, never()).deleteComplex(any());
        }

        @Test
        @DisplayName("Delete complex not found returns 404")
        void delete_complex_not_found_returns_404() throws Exception {
            TestUtils.mockJwtWithRole(jwtTokenProvider, "ADMIN", ADMIN_ID, VALID_TOKEN);
            doThrow(
                            new NotFoundException("Complex not found"))
                    .when(adminSvc)
                    .deleteComplex(COMPLEX_ID);
            mockMvc.perform(
                            delete("/residential-complexes/" + COMPLEX_ID)
                                    .header("Authorization", "Bearer " + VALID_TOKEN))
                    .andExpect(status().isNotFound());
        }
    }
}
