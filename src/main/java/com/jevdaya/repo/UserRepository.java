package com.jevdaya.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jevdaya.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPanCard(String panCard);
    Optional<User> findByAadhaarCard(String aadhaarCard);
//    boolean existsByPhoneNumber(String phoneNumber);
}