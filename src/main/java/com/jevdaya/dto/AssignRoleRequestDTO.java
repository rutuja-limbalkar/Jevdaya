package com.jevdaya.dto;


import lombok.Data;

@Data
public class AssignRoleRequestDTO {
    private String email;        // email of the user you want to update
    private String roleName;     // "ROLE_ADMIN" or "ROLE_USER"
}
