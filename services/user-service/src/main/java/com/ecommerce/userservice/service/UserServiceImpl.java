package com.ecommerce.userservice.service;


import com.ecommerce.userservice.dto.RegisterUserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register (RegisterUserRequest request) {

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        User savedUser = userRepo.save(user);

        return  new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        );
    }

}
