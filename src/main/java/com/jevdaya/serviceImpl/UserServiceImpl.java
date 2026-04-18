package com.jevdaya.serviceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;import com.jevday.util.AESUtil;
import com.jevdaya.UserDTO;
import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;import com.jevdaya.dto.ApiResponse;
import com.jevdaya.dto.AssignRoleRequestDTO;
import com.jevdaya.repo.RoleRepository;
import com.jevdaya.repo.UserRepository;
import com.jevdaya.service.UserService;import jakarta.transaction.Transactional;@Service
public class UserServiceImpl implements UserService {@Autowired
private UserRepository repo;@Autowired
private RoleRepository roleRepository;@Autowired
private PasswordEncoder passwordEncoder;@Transactional
@Override
public ApiResponse registerUser(User user) {//  NAME VALIDATION
if (user.getName() == null || user.getName().isBlank()) {
    return new ApiResponse("Name is required", false);
}

String name = user.getName().trim();
if (!name.matches("^[A-Za-z\\s]+$")) {
    return new ApiResponse("Name must contain only alphabets and spaces", false);
}
if (name.length() < 2) {
    return new ApiResponse("Name must be at least 2 characters long", false);
}
if (name.length() > 50) {
    return new ApiResponse("Name must not exceed 50 characters", false);
}

//  EMAIL VALIDATION
if (user.getEmail() == null || user.getEmail().isBlank()) {
    return new ApiResponse("Email is required", false);
}

String email = user.getEmail().trim();
if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    return new ApiResponse("Invalid email format", false);
}
if (email.length() > 100) {
    return new ApiResponse("Email must not exceed 100 characters", false);
}

//  PASSWORD VALIDATION
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
if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
    return new ApiResponse("Password must contain at least one special character", false);
}

//  PAN VALIDATION
if (user.getPanCard() == null || user.getPanCard().isBlank()) {
    return new ApiResponse("PAN is required", false);
}

//  AADHAAR VALIDATION
if (user.getAadhaarCard() == null || user.getAadhaarCard().isBlank()) {
    return new ApiResponse("Aadhaar is required", false);
}

String pan = user.getPanCard().trim().toUpperCase();
String aadhaar = user.getAadhaarCard().trim();

if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}")) {
    return new ApiResponse("Invalid PAN", false);
}

if (!aadhaar.matches("\\d{12}")) {
    return new ApiResponse("Invalid Aadhaar", false);
}

//  Duplicate email
if (repo.existsByEmail(email)) {
    return new ApiResponse("Email already exists", false);
}

//  Encrypt PAN & Aadhaar FIRST (before duplicate check)
String encryptedPan = AESUtil.encrypt(pan);
String encryptedAadhaar = AESUtil.encrypt(aadhaar);

//  Efficient Duplicate PAN check (No findAll())
if (repo.findByPanCard(encryptedPan).isPresent()) {
    return new ApiResponse("PAN already exists", false);
}

//  Efficient Duplicate Aadhaar check
if (repo.findByAadhaarCard(encryptedAadhaar).isPresent()) {
    return new ApiResponse("Aadhaar already exists", false);
}

// Set validated & cleaned values
user.setName(name);
user.setEmail(email);

//  Encrypt password
user.setPassword(passwordEncoder.encode(password));

// Set encrypted PAN & Aadhaar
user.setPanCard(encryptedPan);
user.setAadhaarCard(encryptedAadhaar);

//  Assign default ROLE_USER (Many-to-Many)
Optional<Role> userRoleOpt = roleRepository.findByName("ROLE_USER");
if (userRoleOpt.isPresent()) {
    user.addRole(userRoleOpt.get());
} else {
    // Fallback (should not happen if DataSeeder ran)
    System.err.println("WARNING: ROLE_USER not found in database!");
}

repo.save(user);

return new ApiResponse("User Registered Successfully", true);}
@Override
public List<UserDTO> getAllUsers() {
    return repo.findAll()
            .stream()
            .map(u -> {
                // Convert the Set<Role> to a comma-separated String
                String rolesString = u.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", "));
                
                // Ensure your UserDTO has a 3-argument constructor: Name, Email, Role
                return new UserDTO(u.getName(), u.getEmail(), rolesString);
            })
            .toList();
}
@Transactional
@Override
public ApiResponse assignRole(AssignRoleRequestDTO request) {
    // 1. Find the User
    Optional<User> userOpt = repo.findByEmail(request.getEmail());
    if (userOpt.isEmpty()) {
        return new ApiResponse("User not found with email: " + request.getEmail(), false);
    }
    User user = userOpt.get();

    // 2. Find the New Role
    Optional<Role> roleOpt = roleRepository.findByName(request.getRoleName());
    if (roleOpt.isEmpty()) {
        return new ApiResponse("Role " + request.getRoleName() + " does not exist", false);
    }
    Role newRole = roleOpt.get();

    // 3. CORE CHANGE: Clear existing roles to "Switch" the role
    // This allows you to demote an Admin to User or promote a Manager to Admin
    user.getRoles().clear(); 
    user.addRole(newRole);

    repo.save(user);

    return new ApiResponse("User " + user.getName() + " is now assigned as " + request.getRoleName(), true);
}
}