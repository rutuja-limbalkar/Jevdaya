package com.jevdaya.test;

import com.jevdaya.CustomUserDetailsService;
import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;
import com.jevdaya.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ 1. User found
    @Test
    void shouldReturnUserDetails_whenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
    }

    // ❌ 2. User not found
    @Test
    void shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test@example.com");
        });
    }

    // ✅ 3. Multiple roles mapping
    @Test
    void shouldMapRolesToAuthorities() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);

        user.setRoles(roles);

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals(2, result.getAuthorities().size());
    }
}