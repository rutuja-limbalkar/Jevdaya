package com.jevdaya.service;

import com.jevdaya.dto.ApiResponse;
import com.jevdaya.dto.AssignRoleRequestDTO;
import com.jevdaya.dto.LoginRequestDTO;
import com.jevdaya.dto.LoginResponseDTO;

public interface AuthService {
	LoginResponseDTO login(LoginRequestDTO request);
	
	 ApiResponse assignRole(AssignRoleRequestDTO request);
}
