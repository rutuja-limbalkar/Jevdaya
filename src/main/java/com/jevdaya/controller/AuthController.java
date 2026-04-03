package com.jevdaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jevdaya.dto.ApiResponse;
import com.jevdaya.dto.AssignRoleRequestDTO;
import com.jevdaya.dto.LoginRequestDTO;
import com.jevdaya.dto.LoginResponseDTO;
import com.jevdaya.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
    @PostMapping("/assign-role")
    public ApiResponse assignRole(@RequestBody AssignRoleRequestDTO request) {
        return authService.assignRole(request);
}
}