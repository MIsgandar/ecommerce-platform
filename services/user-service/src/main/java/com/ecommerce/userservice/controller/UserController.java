package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.RegisterUserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request)
    {
        return userService.register(request);
    }

}
