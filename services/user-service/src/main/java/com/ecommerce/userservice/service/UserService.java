package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.*;

import java.util.UUID;

public interface UserService {

    UserResponse register(RegisterUserRequest request);

    AuthenticationResponse login(LoginRequest request);

    UserProfileResponse getCurrentUser(String email);

    ProductResponse getProduct(UUID productId);
}
