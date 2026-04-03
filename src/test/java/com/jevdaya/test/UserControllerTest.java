package com.jevdaya.test;import com.jevdaya.Entity.User;
import com.jevdaya.JwtAuthenticationFilter;
import com.jevdaya.JwtUtil;
import com.jevdaya.UserDTO;
import com.jevdaya.controller.UserController;
import com.jevdaya.dto.ApiResponse;
import com.jevdaya.service.UserService;import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;import java.util.List;import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)   // This temporarily disables security filters for this test only
class UserControllerTest {@Autowired
private MockMvc mockMvc;

@MockitoBean
private UserService service;

@MockitoBean
private JwtUtil jwtUtil; // ✅ REQUIRED

@MockitoBean
private JwtAuthenticationFilter jwtAuthenticationFilter; // 
@Test
void register_shouldReturnApiResponse() throws Exception {
    User user = new User();
    ApiResponse response = new ApiResponse("User registered successfully", true);

    when(service.registerUser(user)).thenReturn(response);

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))   // This fixes CSRF + helps with auth
            .andExpect(status().isOk());
}

@Test
void getAllUsers_shouldReturnListOfUserDTO() throws Exception {
    List<UserDTO> users = List.of(new UserDTO(), new UserDTO());

    when(service.getAllUsers()).thenReturn(users);

    mockMvc.perform(get("/api/users/all"))
            .andExpect(status().isOk());
}}

