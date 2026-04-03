package com.jevdaya.controller;import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jevdaya.UserDTO;
import com.jevdaya.Entity.User;

import com.jevdaya.dto.ApiResponse;
import com.jevdaya.service.UserService;@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {@Autowired
private UserService service;

@PostMapping("/register")
public ApiResponse register(@RequestBody User user) {
    return service.registerUser(user);
}

@GetMapping("/all")
public List<UserDTO> getAllUsers() {
    return service.getAllUsers();
}


}


//
//    // ✅ ADD THIS - OtpService injected
//    @Autowired
//    private OtpService otpService;
//
//    // ✅ YOUR ORIGINAL register endpoint - KEPT AS IS
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse> register(@RequestBody User user) {
//        ApiResponse response = userService.registerUser(user);
//        return ResponseEntity.ok(response);
//    }
//
//    // ✅ NEW - Send OTP to email
//    @PostMapping("/send-otp")
//    public ResponseEntity<ApiResponse> sendOtp(@RequestBody Map<String, String> body) {
//        String email = body.get("email");
//
//        if (email == null || email.isBlank()) {
//            return ResponseEntity.badRequest().body(new ApiResponse("Email is required", false));
//        }
//
//        try {
//            otpService.sendOtp(email);
//            return ResponseEntity.ok(new ApiResponse("OTP sent to your email", true));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(new ApiResponse("Failed to send OTP. Check email.", false));
//        }
//    }
//
//    // ✅ NEW - Verify OTP and then Register
//    @PostMapping("/verify-otp-and-register")
//    public ResponseEntity<ApiResponse> verifyOtpAndRegister(@RequestBody Map<String, Object> body) {
//        String email = (String) body.get("email");
//        String otp = (String) body.get("otp");
//        String name = (String) body.get("name");
//        String password = (String) body.get("password");
//        String phoneNumber = (String) body.get("phoneNumber");
//
//        // ✅ Step 1: Verify OTP first
//        boolean isOtpValid = otpService.verifyOtp(email, otp);
//        if (!isOtpValid) {
//            return ResponseEntity.badRequest().body(new ApiResponse("Invalid or expired OTP", false));
//        }
//
//        // ✅ Step 2: If OTP correct, register user
//        User user = new User();
//        user.setName(name);
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setPhoneNumber(phoneNumber);
//
//        ApiResponse response = userService.registerUser(user);
//        return ResponseEntity.ok(response);
//    }

    