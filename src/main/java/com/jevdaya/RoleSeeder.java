package com.jevdaya;

import com.jevdaya.Entity.Role;
import com.jevdaya.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("🚀 Starting Role Seeding...");

        // Create ROLE_USER if not exists
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Regular User with basic access");
            roleRepository.save(userRole);
            System.out.println("✅ ROLE_USER created");
        } else {
            System.out.println("ℹ️ ROLE_USER already exists");
        }

        // Create ROLE_ADMIN if not exists
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Administrator with full access");
            roleRepository.save(adminRole);
            System.out.println("✅ ROLE_ADMIN created");
        } else {
            System.out.println("ℹ️ ROLE_ADMIN already exists");
        }

        System.out.println("✅ Role Seeding Completed!");
    }
}