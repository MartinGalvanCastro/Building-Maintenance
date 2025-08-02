package com.martin.buildingmaintenance.adapters.in.web;

import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.util.UUID;

public class TestUtils {
    public static void mockJwtWithRole(
            JwtTokenProvider jwtTokenProvider, String role, UUID subject, String token) {
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(role);
        when(claims.get("role", String.class)).thenReturn(role);
        when(claims.getSubject()).thenReturn(subject.toString());
        Jws<Claims> jws = mock(Jws.class);
        when(jws.getBody()).thenReturn(claims);
        when(jwtTokenProvider.validateToken(anyString()))
                .thenAnswer(
                        invocation -> {
                            String t = invocation.getArgument(0);
                            if (token.equals(t)) return jws;
                            throw new JwtException("Invalid token");
                        });
    }
}
