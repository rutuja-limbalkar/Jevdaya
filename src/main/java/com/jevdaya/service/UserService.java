package com.jevdaya.service;


import java.util.List;

import com.jevdaya.UserDTO;
import com.jevdaya.Entity.User;
import com.jevdaya.dto.ApiResponse;
import com.jevdaya.dto.AssignRoleRequestDTO;

public interface UserService {

	ApiResponse registerUser(User user);

    List<UserDTO> getAllUsers();
    
    
}