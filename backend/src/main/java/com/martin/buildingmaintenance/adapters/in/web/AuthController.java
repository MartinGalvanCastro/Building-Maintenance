package com.martin.buildingmaintenance.adapters.in.web;

import com.martin.buildingmaintenance.application.dto.CredentialsDto;
import com.martin.buildingmaintenance.application.dto.LogInResultDto;
import com.martin.buildingmaintenance.application.port.in.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user authentication, login, and logout.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Authenticate user and return JWT token",
            description =
                    "Authenticates a user with email and password. Returns a JWT token if successful.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful authentication, returns JWT token",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = LogInResultDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request body or validation error",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LogInResultDto login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "User credentials",
                            required = true,
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(implementation = CredentialsDto.class)))
                    @Valid
                    @RequestBody
                    CredentialsDto creds) {
        return authService.authenticate(creds);
    }

    @Operation(
            summary = "Logout user",
            description = "Logs out the user by invalidating the JWT token.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Logout successful"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Missing or malformed Authorization header",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid or expired token",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(
                        responseCode = "403",
                        description = "User not authorized to perform this action",
                        content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        String token =
                bearer != null && bearer.startsWith("Bearer ") ? bearer.substring(7) : bearer;
        authService.logout(token);
    }

    @Operation(
            summary = "Check if a JWT token is valid",
            description = "Returns 200 if the provided JWT token is valid, 401 if not.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "401", description = "Token is invalid or expired")
    })
    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.OK)
    public void validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;
        authService.validateToken(token);
    }
}
