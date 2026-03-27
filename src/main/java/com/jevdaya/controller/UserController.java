package com.jevdaya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jevdaya.UserDTO;
import com.jevdaya.Entity.User;
import com.jevdaya.UserService.UserService;
import com.jevdaya.dto.ApiResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
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