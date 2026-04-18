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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;   // ← Add this

    // In-memory OTP storage (email -> OTP + Expiry). Good for development.
    // For production, use Redis or a proper Otp entity in database.
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private static class OtpData {
        String otp;
        LocalDateTime expiry;

        OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }

    // ==================== Original Methods (Unchanged) ====================
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

    // ==================== New Forgot Password Methods ====================

    @Override
    public void sendResetOTP(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("No account found with this email");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Store with 10 minutes expiry
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        otpStorage.put(email, new OtpData(otp, expiry));

        // Send Email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Jevdaya - Password Reset OTP");
            message.setText("Dear User,\n\nYour OTP for password reset is: " + otp +
                    "\n\nThis OTP is valid for 10 minutes.\n\nIf you didn't request this, please ignore this email.");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email. Please try again later.");
        }
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        OtpData data = otpStorage.get(email);

        if (data == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(data.expiry)) {
            otpStorage.remove(email); // Clean expired OTP
            return false;
        }

        if (!data.otp.equals(otp)) {
            return false;
        }

        // OTP is valid - keep it for reset step (or you can remove here if you want one-time use)
        return true;
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        // Optional: Verify OTP was used recently (extra security)
        if (!otpStorage.containsKey(email)) {
            throw new RuntimeException("OTP verification required before resetting password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Clean up OTP after successful reset
        otpStorage.remove(email);
    }
}