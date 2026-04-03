package com.jevdaya.test;

import com.jevdaya.JwtAuthenticationFilter;
import com.jevdaya.JwtUtil;
import com.jevdaya.controller.AuthController;
import com.jevdaya.dto.LoginRequestDTO;
import com.jevdaya.dto.LoginResponseDTO;
import com.jevdaya.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)   // This temporarily disables security filters for this test only
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil; // ✅ REQUIRED
    
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter; // ✅ ADD THIS (VERY IMPORTANT)

    @Test
    void login_shouldReturnLoginResponse() throws Exception {
        // Prepare test data
        LoginRequestDTO request = new LoginRequestDTO();
        LoginResponseDTO response = new LoginResponseDTO();

        // Mock service behavior
        when(authService.login(request)).thenReturn(response);

        // Perform test
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")                                      // empty JSON for simplicity
                .with(SecurityMockMvcRequestPostProcessors.csrf())) // Required because of Spring Security
                .andExpect(status().isOk());
    }
}