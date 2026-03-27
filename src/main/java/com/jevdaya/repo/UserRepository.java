package com.jevdaya.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jevdaya.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByPanCard(String panCard);
    boolean existsByAadhaarCard(String aadhaarCard);
    boolean existsByEmail(String email);
}