package com.jevdaya.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String panCard;

    // ✅ Aadhaar Card
    @Column(unique = true)
    private String aadhaarCard;


//    // Phone Number added - only field, NO validation annotations
//    @Column(nullable = false, unique = true)
//    private String phoneNumber;

    // Many-to-Many with Roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Helper method
    public void addRole(Role role) {
        this.roles.add(role);
    }
}