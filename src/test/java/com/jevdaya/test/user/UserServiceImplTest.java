package com.jevdaya.test.user;

import com.jevdaya.UserDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        user.setName("Rutuja Jain");
        user.setEmail("test@example.com");
        user.setPassword("Password@123");
        user.setPanCard("ABCDE1234F");
        user.setAadhaarCard("123456789012");
    }

    // ==================== NAME VALIDATION ====================
    @Test
    void shouldFailWhenNameIsNull() {
        user.setName(null);
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Name is required", res.getMessage());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        user.setName("   ");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Name is required", res.getMessage());
    }

    @Test
    void shouldFailWhenNameContainsSpecialChars() {
        user.setName("Rutuja@Jain");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Name must contain only alphabets and spaces", res.getMessage());
    }

    @Test
    void shouldFailWhenNameTooShort() {
        user.setName("R");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Name must be at least 2 characters long", res.getMessage());
    }

    @Test
    void shouldFailWhenNameTooLong() {
        user.setName("A".repeat(51));
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Name must not exceed 50 characters", res.getMessage());
    }

    // ==================== EMAIL VALIDATION ====================
    @Test
    void shouldFailWhenEmailIsNullOrBlank() {
        user.setEmail(null);
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Email is required", res.getMessage());
    }

    @Test
    void shouldFailWhenEmailInvalidFormat() {
        user.setEmail("invalid-email");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Invalid email format", res.getMessage());
    }

    @Test
    void shouldFailWhenEmailTooLong() {
        user.setEmail("a".repeat(91) + "@example.com");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Email must not exceed 100 characters", res.getMessage());
    }

    // ==================== PASSWORD VALIDATION ====================
    @Test
    void shouldFailWhenPasswordIsNullOrBlank() {
        user.setPassword(null);
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Password is required", res.getMessage());
    }

    @Test
    void shouldFailWhenPasswordTooShort() {
        user.setPassword("Pass@1");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Password must be at least 8 characters long", res.getMessage());
    }

    @Test
    void shouldFailWhenPasswordTooLong() {
        user.setPassword("P".repeat(101));
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Password must not exceed 100 characters", res.getMessage());
    }

    @Test
    void shouldFailWhenPasswordHasNoSpecialChar() {
        user.setPassword("Password123");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Password must contain at least one special character", res.getMessage());
    }

    // ==================== PAN & AADHAAR VALIDATION ====================
    @Test
    void shouldFailWhenPanIsNullOrBlank() {
        user.setPanCard(null);
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("PAN is required", res.getMessage());
    }

    @Test
    void shouldFailWhenPanInvalidFormat() {
        user.setPanCard("ABCDE12345"); // wrong pattern
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Invalid PAN", res.getMessage());
    }

    @Test
    void shouldFailWhenAadhaarIsNullOrBlank() {
        user.setAadhaarCard("   ");
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Aadhaar is required", res.getMessage());
    }

    @Test
    void shouldFailWhenAadhaarInvalid() {
        user.setAadhaarCard("12345678901"); // 11 digits
        ApiResponse res = userService.registerUser(user);
        assertFalse(res.isSuccess());
        assertEquals("Invalid Aadhaar", res.getMessage());
    }

    // ==================== DUPLICATE CHECKS ====================
    @Test
    void shouldFailWhenEmailAlreadyExists() {
        when(repo.existsByEmail(anyString())).thenReturn(true);

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
        assertEquals("Email already exists", res.getMessage());
        verify(repo, never()).findByPanCard(any());
    }

    @Test
    void shouldFailWhenPanAlreadyExists() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.of(new User()));

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
        assertEquals("PAN already exists", res.getMessage());
    }

    @Test
    void shouldFailWhenAadhaarAlreadyExists() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.empty());
        when(repo.findByAadhaarCard(anyString())).thenReturn(Optional.of(new User()));

        ApiResponse res = userService.registerUser(user);

        assertFalse(res.isSuccess());
        assertEquals("Aadhaar already exists", res.getMessage());
    }

    // ==================== SUCCESS & ROLE CASES ====================
    @Test
    void shouldRegisterSuccessfullyWithRole() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.empty());
        when(repo.findByAadhaarCard(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        ApiResponse res = userService.registerUser(user);

        assertTrue(res.isSuccess());
        assertEquals("User Registered Successfully", res.getMessage());

        verify(repo).save(argThat(savedUser -> 
            "encodedPassword".equals(savedUser.getPassword()) &&
            savedUser.getPanCard() != null && 
            savedUser.getAadhaarCard() != null &&
            !savedUser.getRoles().isEmpty()
        ));
    }

    @Test
    void shouldRegisterSuccessfullyEvenIfRoleNotFound() {
        when(repo.existsByEmail(anyString())).thenReturn(false);
        when(repo.findByPanCard(anyString())).thenReturn(Optional.empty());
        when(repo.findByAadhaarCard(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        ApiResponse res = userService.registerUser(user);

        assertTrue(res.isSuccess());
        verify(repo).save(any(User.class));
        // You can also check System.err if you want (advanced)
    }

    // ==================== getAllUsers ====================
    @Test
    void shouldReturnUserList() {
        User u1 = new User();
        u1.setName("Rutuja");
        u1.setEmail("rutuja@example.com");

        User u2 = new User();
        u2.setName("Test User");
        u2.setEmail("test@example.com");

        when(repo.findAll()).thenReturn(List.of(u1, u2));

        List<UserDTO> list = userService.getAllUsers();

        assertEquals(2, list.size());
        assertEquals("Rutuja", list.get(0).getName());
        assertEquals("rutuja@example.com", list.get(0).getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        when(repo.findAll()).thenReturn(List.of());

        List<UserDTO> list = userService.getAllUsers();

        assertTrue(list.isEmpty());
    }
}