package com.jevdaya.serviceImpl;

import com.jevdaya.JwtUtil;
import com.jevdaya.Entity.Role;
import com.jevdaya.Entity.User;
import com.jevdaya.dto.ApiResponse;
import com.jevdaya.dto.AssignRoleRequestDTO;
import com.jevdaya.dto.LoginRequestDTO;
import com.jevdaya.dto.LoginResponseDTO;
import com.jevdaya.repo.RoleRepository;
import com.jevdaya.repo.UserRepository;
import com.jevdaya.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;               // Injected here

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new LoginResponseDTO("User not found", null, null, null, null);
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponseDTO("Invalid password", null, null, null, null);
        }

        String token = jwtUtil.generateToken(user);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new LoginResponseDTO(
                "Login Successful",
                user.getEmail(),
                user.getName(),
                token,
                roles
        );
    }
    
    @Transactional
    @Override
    public ApiResponse assignRole(AssignRoleRequestDTO request) {
        // Your original assignRole code (kept unchanged)
        if (request.getEmail() == null || request.getRoleName() == null || 
            request.getEmail().isBlank() || request.getRoleName().isBlank()) {
            return new ApiResponse("Email and roleName are required", false);
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim());
        if (userOpt.isEmpty()) {
            return new ApiResponse("User not found with email: " + request.getEmail(), false);
        }

        User user = userOpt.get();

        Optional<Role> roleOpt = roleRepository.findByName(request.getRoleName().trim());
        if (roleOpt.isEmpty()) {
            return new ApiResponse("Role not found: " + request.getRoleName(), false);
        }

        Role role = roleOpt.get();

        if (user.getRoles().contains(role)) {
            return new ApiResponse("User already has role: " + request.getRoleName(), false);
        }

        user.addRole(role);
        userRepository.save(user);

        return new ApiResponse("Role '" + request.getRoleName() + "' assigned successfully to user", true);
    }
}