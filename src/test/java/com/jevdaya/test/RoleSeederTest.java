package com.jevdaya.test;

import com.jevdaya.RoleSeeder;
import com.jevdaya.Entity.Role;
import com.jevdaya.repo.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class RoleSeederTest {

    private RoleRepository roleRepository;
    private RoleSeeder roleSeeder;

    @BeforeEach
    void setUp() {
        roleRepository = mock(RoleRepository.class);
        roleSeeder = new RoleSeeder(roleRepository);
    }

    // ✅ When roles do NOT exist → should create both
    @Test
    void shouldCreateRolesIfNotExist() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        roleSeeder.run();

        verify(roleRepository, times(2)).save(any(Role.class));
    }

    // ✅ When roles already exist → should NOT create
    @Test
    void shouldNotCreateRolesIfAlreadyExist() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(new Role()));

        roleSeeder.run();

        verify(roleRepository, never()).save(any(Role.class));
    }

    // ✅ Partial case (user exists, admin not)
    @Test
    void shouldCreateOnlyMissingRole() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        roleSeeder.run();

        verify(roleRepository, times(1)).save(any(Role.class));
    }
}