package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.*;

public interface UserService {

    UserResponse register(RegisterUserRequest request);

    AuthenticationResponse login(LoginRequest request);

    UserProfileResponse getCurrentUser(String email);
}
