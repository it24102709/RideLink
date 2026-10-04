package com.ridelink.drivervehicle.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String requestPath = request.getRequestURI();
        
        // Skip JWT filter for auth endpoint and public paths
        if (requestPath.equals("/api/auth/token") || 
            requestPath.startsWith("/api-docs") || 
            requestPath.startsWith("/swagger-ui") ||
            requestPath.equals("/health") ||
            requestPath.equals("/api/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        
        // Accept any Authorization header for testing
        if (authHeader != null && !authHeader.isEmpty()) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    "test_user",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER"))
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
            return;
        }
        
        // If no Authorization header, still allow for now
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                "test_user",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_DRIVER"))
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        
        filterChain.doFilter(request, response);
    }
}
