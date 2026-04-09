package com.jevdaya;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Improved public endpoints list
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/login", "/auth/register", "/auth/assign-role",
            "/api/users/register", "/gallery/","/upload",
            "/gaushala", "/api/gaushala"
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

     // ✅ Allow payment APIs without JWT (TEMPORARY FOR TESTING)
        if (requestURI.startsWith("/payment")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Skip JWT check for all public endpoints
        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // For protected routes only: check JWT
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();

        try {
            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        if (uri == null) return false;

        return PUBLIC_ENDPOINTS.stream().anyMatch(publicPath ->
                uri.equals(publicPath) || uri.startsWith(publicPath + "/")
        ) ||
               uri.startsWith("/auth/") ||
               uri.startsWith("/api/users/") ||
               uri.startsWith("/gaushala/") ||
               uri.startsWith("/gallery/")||
               uri.startsWith("/api/gaushala/");
               
              
    }
}