package com.jevdaya.test;

import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;
import com.jevdaya.dto.ApiResponse;
import com.jevdaya.repo.RoleRepository;
import com.jevdaya.repo.UserRepository;
import com.jevdaya.serviceImpl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repo;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Rutuja");
        user.setEmail("test@example.com");
        user.setPassword("Password@123");
        user.setPanCard("ABCDE1234F");
        user.setAadhaarCard("123456789012");
    }

    // ✅ 1. Name validation
    @Test
    void shouldFailWhenNameBlank() {
        user.setName("");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
    }

    // ✅ 2. Email validation
    @Test
    void shouldFailWhenEmailInvalid() {
        user.setEmail("abc");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
    }

    // ✅ 3. Password validation
    @Test
    void shouldFailWhenPasswordWeak() {
        user.setPassword("123");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
    }

    // ✅ 4. PAN validation
    @Test
    void shouldFailWhenPanInvalid() {
        user.setPanCard("123");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
    }

    // ✅ 5. Aadhaar validation
    @Test
    void shouldFailWhenAadhaarInvalid() {
        user.setAadhaarCard("123");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
    }

    // ✅ 6. Duplicate email
    @Test
    void shouldFailWhenEmailExists() {
        when(repo.existsByEmail(anyString())).thenReturn(true);

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
        assertEquals("Email already exists", res.getMessage());
    }

    // ✅ 7. PAN duplicate
    @Test
    void shouldFailWhenPanExists() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.of(new User()));

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
    }

    // ✅ 8. Aadhaar duplicate
    @Test
    void shouldFailWhenAadhaarExists() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.empty());
        when(repo.findByAadhaarCard(anyString())).thenReturn(Optional.of(new User()));

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
    }

    // ✅ 9. Success case
    @Test
    void shouldRegisterSuccessfully() {

        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.empty());
        when(repo.findByAadhaarCard(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        ApiResponse res = userService.registerUser(user);

        assertTrue(res.isSuccess());
        verify(repo).save(any(User.class));
    }

    // ✅ 10. getAllUsers
    @Test
    void shouldReturnUserList() {
        User u = new User();
        u.setName("Rutuja");
        u.setEmail("test@example.com");

        when(repo.findAll()).thenReturn(List.of(u));

        var list = userService.getAllUsers();

        assertEquals(1, list.size());
        assertEquals("Rutuja", list.get(0).getName());
    }
}