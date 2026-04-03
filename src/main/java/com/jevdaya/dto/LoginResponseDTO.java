package com.jevdaya.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String message;
    private String email;
    private String name;
    private String token;           // NEW
    private Set<String> roles;      // NEW - returns roles
}
