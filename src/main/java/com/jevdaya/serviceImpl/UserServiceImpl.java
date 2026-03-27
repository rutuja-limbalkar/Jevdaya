package com.jevdaya.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jevday.util.AESUtil;
import com.jevdaya.UserDTO;
import com.jevdaya.Entity.User;
import com.jevdaya.UserService.UserService;
import com.jevdaya.dto.ApiResponse;
import com.jevdaya.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ApiResponse registerUser(User user) {

        // ✅ NAME VALIDATION
        if (user.getName() == null || user.getName().isBlank()) {
            return new ApiResponse("Name is required", false);
        }
        
        String name = user.getName().trim();
        
        // Check if name contains only letters and spaces (no numbers or special characters)
        if (!name.matches("^[A-Za-z\\s]+$")) {
            return new ApiResponse("Name must contain only alphabets and spaces", false);
        }
        
        // Check name length (minimum 2, maximum 50 characters)
        if (name.length() < 2) {
            return new ApiResponse("Name must be at least 2 characters long", false);
        }
        
        if (name.length() > 50) {
            return new ApiResponse("Name must not exceed 50 characters", false);
        }

        // ✅ EMAIL VALIDATION
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return new ApiResponse("Email is required", false);
        }

        String email = user.getEmail().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new ApiResponse("Invalid email format", false);
        }
        
        // Check email length (maximum 100 characters)
        if (email.length() > 100) {
            return new ApiResponse("Email must not exceed 100 characters", false);
        }

        // ✅ PASSWORD VALIDATION
        String password = user.getPassword();
        
        if (password == null || password.isBlank()) {
            return new ApiResponse("Password is required", false);
        }
        
        if (password.length() < 8) {
            return new ApiResponse("Password must be at least 8 characters long", false);
        }
        
        if (password.length() > 100) {
            return new ApiResponse("Password must not exceed 100 characters", false);
        }
        
        // Password special character validation
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return new ApiResponse("Password must contain at least one special character", false);
        }

        // ✅ PAN VALIDATION
        if (user.getPanCard() == null || user.getPanCard().isBlank()) {
            return new ApiResponse("PAN is required", false);
        }

        // ✅ AADHAAR VALIDATION
        if (user.getAadhaarCard() == null || user.getAadhaarCard().isBlank()) {
            return new ApiResponse("Aadhaar is required", false);
        }

        String pan = user.getPanCard().trim().toUpperCase();
        String aadhaar = user.getAadhaarCard().trim();

        // 🔹 PAN validation
        if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
            return new ApiResponse("Invalid PAN", false);
        }

        // 🔹 Aadhaar validation
        if (!aadhaar.matches("\\d{12}")) {
            return new ApiResponse("Invalid Aadhaar", false);
        }

        // 🔹 Duplicate email
        if (repo.existsByEmail(email)) {
            return new ApiResponse("Email already exists", false);
        }

        // 🔹 Duplicate PAN
        boolean panExists = repo.findAll().stream()
                .anyMatch(u -> safeDecryptEquals(u.getPanCard(), pan));
        if (panExists) {
            return new ApiResponse("PAN already exists", false);
        }

        // 🔹 Duplicate Aadhaar
        boolean aadhaarExists = repo.findAll().stream()
                .anyMatch(u -> safeDecryptEquals(u.getAadhaarCard(), aadhaar));
        if (aadhaarExists) {
            return new ApiResponse("Aadhaar already exists", false);
        }

        // Set validated values
        user.setName(name);
        user.setEmail(email);

        // 🔐 Encrypt password
        user.setPassword(passwordEncoder.encode(password));

        // 🔐 Encrypt PAN & Aadhaar
        user.setPanCard(AESUtil.encrypt(pan));
        user.setAadhaarCard(AESUtil.encrypt(aadhaar));

        repo.save(user);

        return new ApiResponse("User Registered Successfully", true);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return repo.findAll()
                .stream()
                .map(u -> new UserDTO(u.getName(), u.getEmail()))
                .toList();
    }

    private boolean safeDecryptEquals(String encryptedValue, String plainValue) {
        try {
            return AESUtil.decrypt(encryptedValue).equals(plainValue);
        } catch (Exception e) {
            return false;
        }
    }
}