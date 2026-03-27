package com.jevdaya.UserService;


import java.util.List;

import com.jevdaya.UserDTO;
import com.jevdaya.Entity.User;
import com.jevdaya.dto.ApiResponse;

public interface UserService {

	ApiResponse registerUser(User user);

    List<UserDTO> getAllUsers();
}