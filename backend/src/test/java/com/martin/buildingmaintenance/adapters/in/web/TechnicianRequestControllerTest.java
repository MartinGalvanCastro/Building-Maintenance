package com.martin.buildingmaintenance.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.buildingmaintenance.application.dto.ChangeStatusDto;
import com.martin.buildingmaintenance.application.dto.MaintenanceRequestDto;
import com.martin.buildingmaintenance.application.port.in.TechnicianRequestService;
import com.martin.buildingmaintenance.infrastructure.config.SecurityConfig;
import com.martin.buildingmaintenance.infrastructure.persistence.adapter.BlacklistedTokenAdapter;
import com.martin.buildingmaintenance.security.JwtTokenProvider;
import com.martin.buildingmaintenance.domain.model.RequestStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.martin.buildingmaintenance.adapters.in.web.TestUtils.mockJwtWithRole;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(controllers = TechnicianRequestController.class)
@Import(SecurityConfig.class)
class TechnicianRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private TechnicianRequestService svc;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private BlacklistedTokenAdapter tokenBlacklist;

    private static final String VALID_TOKEN = "valid-token";
    private static final UUID TECHNICIAN_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID REQUEST_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void list_should_return_requests_for_technician() throws Exception {
        mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", TECHNICIAN_ID, VALID_TOKEN);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(svc.listAssignments(TECHNICIAN_ID)).thenReturn(List.of(dto));
        mockMvc.perform(get("/technicians/" + TECHNICIAN_ID + "/requests")
                .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk());
        verify(svc).listAssignments(TECHNICIAN_ID);
    }

    @Test
    void changeStatus_should_return_updated_request() throws Exception {
        mockJwtWithRole(jwtTokenProvider, "TECHNICIAN", TECHNICIAN_ID, VALID_TOKEN);
        ChangeStatusDto changeStatusDto = new ChangeStatusDto(RequestStatus.IN_PROGRESS);
        MaintenanceRequestDto responseDto = mock(MaintenanceRequestDto.class);
        when(svc.changeStatus(TECHNICIAN_ID, REQUEST_ID, changeStatusDto)).thenReturn(responseDto);
        String json = objectMapper.writeValueAsString(changeStatusDto);
        mockMvc.perform(patch("/technicians/" + TECHNICIAN_ID + "/requests/" + REQUEST_ID + "/status")
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        verify(svc).changeStatus(TECHNICIAN_ID, REQUEST_ID, changeStatusDto);
    }

    @Test
    void list_should_return_403_for_invalid_token() throws Exception {
        when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
        mockMvc.perform(get("/technicians/" + TECHNICIAN_ID + "/requests")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isForbidden());
        verify(svc, never()).listAssignments(any());
    }

    @Test
    void changeStatus_should_return_403_for_invalid_token() throws Exception {
        when(jwtTokenProvider.validateToken(anyString())).thenThrow(new io.jsonwebtoken.JwtException("Invalid token"));
        ChangeStatusDto changeStatusDto = new ChangeStatusDto(RequestStatus.IN_PROGRESS);
        String json = objectMapper.writeValueAsString(changeStatusDto);
        mockMvc.perform(patch("/technicians/" + TECHNICIAN_ID + "/requests/" + REQUEST_ID + "/status")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
        verify(svc, never()).changeStatus(any(), any(), any());
    }

    @Test
    void list_should_return_403_for_non_technician_or_admin() throws Exception {
        mockJwtWithRole(jwtTokenProvider, "RESIDENT", TECHNICIAN_ID, VALID_TOKEN);
        mockMvc.perform(get("/technicians/" + TECHNICIAN_ID + "/requests")
                .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isForbidden());
        verify(svc, never()).listAssignments(any());
    }

    @Test
    void changeStatus_should_return_403_for_non_technician_or_admin() throws Exception {
        mockJwtWithRole(jwtTokenProvider, "RESIDENT", TECHNICIAN_ID, VALID_TOKEN);
        ChangeStatusDto changeStatusDto = new ChangeStatusDto(RequestStatus.IN_PROGRESS);
        String json = objectMapper.writeValueAsString(changeStatusDto);
        mockMvc.perform(patch("/technicians/" + TECHNICIAN_ID + "/requests/" + REQUEST_ID + "/status")
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
        verify(svc, never()).changeStatus(any(), any(), any());
    }

    @Test
    void list_should_return_requests_for_admin() throws Exception {
        mockJwtWithRole(jwtTokenProvider ,"ADMIN", TECHNICIAN_ID, VALID_TOKEN);
        MaintenanceRequestDto dto = mock(MaintenanceRequestDto.class);
        when(svc.listAssignments(TECHNICIAN_ID)).thenReturn(List.of(dto));
        mockMvc.perform(get("/technicians/" + TECHNICIAN_ID + "/requests")
                .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk());
        verify(svc).listAssignments(TECHNICIAN_ID);
    }

    @Test
    void changeStatus_should_return_updated_request_for_admin() throws Exception {
        mockJwtWithRole(jwtTokenProvider, "ADMIN", TECHNICIAN_ID, VALID_TOKEN);
        ChangeStatusDto changeStatusDto = new ChangeStatusDto(RequestStatus.IN_PROGRESS);
        MaintenanceRequestDto responseDto = mock(MaintenanceRequestDto.class);
        when(svc.changeStatus(TECHNICIAN_ID, REQUEST_ID, changeStatusDto)).thenReturn(responseDto);
        String json = objectMapper.writeValueAsString(changeStatusDto);
        mockMvc.perform(patch("/technicians/" + TECHNICIAN_ID + "/requests/" + REQUEST_ID + "/status")
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        verify(svc).changeStatus(TECHNICIAN_ID, REQUEST_ID, changeStatusDto);
    }
}
