package com.prepnovis.backend.service;

import com.prepnovis.backend.dto.request.RegisterUserRequest;
import com.prepnovis.backend.dto.response.RegisterUserResponse;

public interface AuthService {

    RegisterUserResponse register(RegisterUserRequest request);

    

}