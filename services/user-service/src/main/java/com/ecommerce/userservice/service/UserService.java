package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.AuthenticationResponse;
import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.RegisterUserRequest;
import com.ecommerce.userservice.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterUserRequest request);

    AuthenticationResponse login(LoginRequest request);
}
