package com.jevdaya.test;

import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;
import com.jevdaya.JwtUtil;
import com.jevdaya.dto.*;
import com.jevdaya.repo.RoleRepository;
import com.jevdaya.repo.UserRepository;
import com.jevdaya.serviceImpl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setName("Rutuja");
        user.setPassword("encodedPassword");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(new java.util.HashSet<>(Set.of(role))); // ✅
    }

    // ✅ 1. User not found
    @Test
    void shouldReturnUserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        LoginResponseDTO response = authService.login(request);

        assertEquals("User not found", response.getMessage());
    }

    // ✅ 2. Invalid password
    @Test
    void shouldReturnInvalidPassword() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("wrong");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginResponseDTO response = authService.login(request);

        assertEquals("Invalid password", response.getMessage());
    }

    // ✅ 3. Successful login
    @Test
    void shouldLoginSuccessfully() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("mock-token");

        LoginResponseDTO response = authService.login(request);

        assertEquals("Login Successful", response.getMessage());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("mock-token", response.getToken());
    }

    // ✅ 4. assignRole - invalid input
    @Test
    void shouldFailWhenInputInvalid() {
        AssignRoleRequestDTO request = new AssignRoleRequestDTO();
        request.setEmail("");
        request.setRoleName("");

        ApiResponse response = authService.assignRole(request);

        assertFalse(response.isSuccess());
    }

    // ✅ 5. User not found
    @Test
    void shouldFailWhenUserNotFound() {
        AssignRoleRequestDTO request = new AssignRoleRequestDTO();
        request.setEmail("test@example.com");
        request.setRoleName("ROLE_ADMIN");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ApiResponse response = authService.assignRole(request);

        assertFalse(response.isSuccess());
    }

    // ✅ 6. Role not found
    @Test
    void shouldFailWhenRoleNotFound() {
        AssignRoleRequestDTO request = new AssignRoleRequestDTO();
        request.setEmail("test@example.com");
        request.setRoleName("ROLE_ADMIN");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        ApiResponse response = authService.assignRole(request);

        assertFalse(response.isSuccess());
    }

    // ✅ 7. Role already exists
    @Test
    void shouldFailWhenRoleAlreadyExists() {
        AssignRoleRequestDTO request = new AssignRoleRequestDTO();
        request.setEmail("test@example.com");
        request.setRoleName("ROLE_USER");

        Role role = new Role();
        role.setName("ROLE_USER");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        ApiResponse response = authService.assignRole(request);

        assertFalse(response.isSuccess());
    }

    // ✅ 8. Successful role assign
    @Test
    void shouldAssignRoleSuccessfully() {
        AssignRoleRequestDTO request = new AssignRoleRequestDTO();
        request.setEmail("test@example.com");
        request.setRoleName("ROLE_ADMIN");

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        ApiResponse response = authService.assignRole(request);

        assertTrue(response.isSuccess());
        verify(userRepository).save(user);
    }
}