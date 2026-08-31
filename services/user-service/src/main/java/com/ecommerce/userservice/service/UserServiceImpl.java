package com.ecommerce.userservice.service;


import com.ecommerce.userservice.client.ProductClient;
import com.ecommerce.userservice.dto.*;
import com.ecommerce.userservice.entity.Role;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.UserRepo;
import com.ecommerce.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProductClient productClient;

    public UserResponse register (RegisterUserRequest request) {

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.CUSTOMER)
                .build();

        User savedUser = userRepo.save(user);

        return  new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        );
    }

    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );


        String token = jwtService.generateToken(request.email());

        return new AuthenticationResponse(token);
    }

    @Override
    public UserProfileResponse getCurrentUser(String email) {

        User user = userRepo.findByEmail(email)

                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserProfileResponse(

                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()

        );

    }

    public ProductResponse getProduct(UUID productId) {

        return productClient.getProduct(productId);
    }

}
