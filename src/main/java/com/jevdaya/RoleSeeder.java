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

        createRoleIfNotExists("ROLE_USER", "Regular User with basic access");
        createRoleIfNotExists("ROLE_MANAGER", "Manager with moderate access - can manage team and resources");
        createRoleIfNotExists("ROLE_ADMIN", "Administrator with full access");

        System.out.println("✅ Role Seeding Completed!");
    }

    private void createRoleIfNotExists(String roleName, String description) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            roleRepository.save(role);
            System.out.println("✅ " + roleName + " created");
        } else {
            System.out.println("ℹ️ " + roleName + " already exists");
        }
    }
}