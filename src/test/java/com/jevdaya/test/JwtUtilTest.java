package com.jevdaya.test;

import com.jevdaya.JwtUtil;
import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    // ✅ Test Token Generation + Validation
    @Test
    void shouldGenerateAndValidateToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(Set.of(role));

        String token = jwtUtil.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    // ✅ Test Extract Email
    @Test
    void shouldExtractEmailFromToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(Set.of(role));

        String token = jwtUtil.generateToken(user);

        String email = jwtUtil.extractEmail(token);

        assertEquals("test@example.com", email);
    }

    // ✅ Test Extract Roles
    @Test
    void shouldExtractRolesFromToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");

        user.setRoles(Set.of(role1, role2));

        String token = jwtUtil.generateToken(user);

        Set<String> roles = jwtUtil.extractRoles(token);

        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    // ✅ Test Invalid Token
    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";

        boolean result = jwtUtil.validateToken(invalidToken);

        assertFalse(result);
    }
}