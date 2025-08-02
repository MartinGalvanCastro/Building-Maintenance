package com.martin.buildingmaintenance.security;

import com.martin.buildingmaintenance.application.port.out.TokenBlacklistRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final TokenBlacklistRepository blacklist;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                var claimsJws = jwtProvider.validateToken(token);
                var claims = claimsJws.getBody();

                if (blacklist.isRevoked(token)) {
                    throw new JwtException("Invalid token");
                }

                UUID userId = UUID.fromString(claims.getSubject());
                String role = claims.get("role", String.class);

                var auth =
                        new UsernamePasswordAuthenticationToken(
                                new org.springframework.security.core.userdetails.User(
                                        userId.toString(), "", List.of(() -> "ROLE_" + role)),
                                null,
                                List.of(() -> "ROLE_" + role));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException ex) {
                // invalid token
            }
        }
        chain.doFilter(request, response);
    }
}
