package com.jevdaya.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;        // "ROLE_ADMIN", "ROLE_USER"

    private String description;

    // Optional: Many-to-Many with Permissions later
    // private Set<Permission> permissions = new HashSet<>();
}