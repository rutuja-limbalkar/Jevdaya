package com.jevdaya;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // These remain open to everyone
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
        "/auth/login",
        "/auth/register",
        "/auth/forgot-password",
        "/auth/verify-otp",
        "/auth/reset-password",
        "/api/users/register",
        "/gallery",
        "/upload",
        "/gaushala",
        "/api/gaushala",
        "/api/pages",
        "/payment/offline"
    );

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String header = request.getHeader("Authorization");

     // Inside your doFilterInternal method
     // Inside doFilterInternal
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    
                    // 1. EXTRACT ROLES FROM JWT
                    Set<String> roles = jwtUtil.extractRoles(token); 
                    
                    // 2. CONVERT STRINGS TO GRANTED AUTHORITIES
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    // 3. PASS THE AUTHORITIES HERE (Crucial for hasAnyRole to work)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                authorities // This now contains [ROLE_ADMIN] or [ROLE_MANAGER]
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        

        // 2. Continue to SecurityConfig logic
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(publicPath ->
                uri.equals(publicPath) || uri.startsWith(publicPath + "/")
        );
    }
}