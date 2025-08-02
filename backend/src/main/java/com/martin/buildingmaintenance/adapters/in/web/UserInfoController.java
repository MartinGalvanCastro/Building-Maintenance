package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.UserInfoDto;
import com.martin.buildingmaintenance.application.port.in.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Info",
        description =
                "Endpoints for retrieving information about the currently authenticated user.")
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Operation(
            summary = "Get current user info",
            description =
                    "Returns information about the currently authenticated user, including their role, relationships, and related maintenance requests.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "User info returned successfully"),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - Invalid or missing token"),
                @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping
    public ResponseEntity<UserInfoDto> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return ResponseEntity.ok(userInfoService.getCurrentUserInfo(authHeader));
    }
}
