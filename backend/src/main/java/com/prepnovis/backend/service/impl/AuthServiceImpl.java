package com.prepnovis.backend.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prepnovis.backend.dto.request.RegisterUserRequest;
import com.prepnovis.backend.dto.response.RegisterUserResponse;
import com.prepnovis.backend.repository.RoleRepository;
import com.prepnovis.backend.repository.UserRepository;
import com.prepnovis.backend.service.AuthService;
import com.prepnovis.backend.entity.Role;
import com.prepnovis.backend.entity.User;
import com.prepnovis.backend.exception.EmailAlreadyExistsException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

 @Override
public RegisterUserResponse register(RegisterUserRequest request) {

    // Step 1: Check if email already exists
   if (userRepository.existsByEmail(request.getEmail())) {
    throw new EmailAlreadyExistsException(
            "Email '" + request.getEmail() + "' is already registered."
    );
}

    // Step 2: Fetch USER role
    Role role = roleRepository.findByName("USER")
            .orElseThrow(() -> new RuntimeException("Default role USER not found."));

    // Step 3: Create User entity
    User user = new User();
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(role);

    // Step 4: Save User
    User savedUser = userRepository.save(user);

    // Step 5: Prepare Response
    RegisterUserResponse response = new RegisterUserResponse();
    response.setId(savedUser.getId());
    response.setFullName(savedUser.getFullName());
    response.setEmail(savedUser.getEmail());
    response.setRole(savedUser.getRole().getName());
    response.setMessage("User registered successfully.");

    return response;
}
}