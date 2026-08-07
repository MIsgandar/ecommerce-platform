package com.ecommerce.userservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static Authentication getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(authentication == null) {
            throw new IllegalStateException(
                    "No authenticated user found"
            );
        }

        return authentication;
    }

}
