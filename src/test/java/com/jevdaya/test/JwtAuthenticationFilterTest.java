package com.jevdaya.test;


import com.jevdaya.JwtAuthenticationFilter;
import com.jevdaya.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private JwtAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(UserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        filterChain = mock(FilterChain.class);
    }

    // ✅ 1. Test Public Endpoint (should skip JWT)
    @Test
    void shouldSkipJwtForPublicEndpoint() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/auth/login");

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtUtil);
    }

    // ✅ 2. Test No Authorization Header
    @Test
    void shouldContinueWhenNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    // ✅ 3. Test Invalid Token
    @Test
    void shouldNotAuthenticateWhenTokenInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "Bearer invalid-token");

        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(jwtUtil).validateToken("invalid-token");
        verify(filterChain).doFilter(request, response);
    }

    // ✅ 4. Test Valid Token (Authentication Success)
    @Test
    void shouldAuthenticateWhenTokenValid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "Bearer valid-token");

        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(jwtUtil.extractEmail("valid-token")).thenReturn("test@example.com");

        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(jwtUtil).validateToken("valid-token");
        verify(jwtUtil).extractEmail("valid-token");
        verify(userDetailsService).loadUserByUsername("test@example.com");
        verify(filterChain).doFilter(request, response);
    }

    // ✅ 5. Test Exception Case (user not found)
    @Test
    void shouldHandleExceptionAndContinue() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "Bearer valid-token");

        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(jwtUtil.extractEmail("valid-token")).thenReturn("test@example.com");

        when(userDetailsService.loadUserByUsername("test@example.com"))
                .thenThrow(new RuntimeException("User not found"));

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
