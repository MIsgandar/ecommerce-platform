package com.ecommerce.userservice.security;

import org.springframework.security.core.Authentication;

public final class AuthorizationUtils {

    private AuthorizationUtils() {

    }

    public static boolean hasRole(

            Authentication authentication,
            String role
    ) {

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                        authority ->
                                authority.getAuthority().equals(
                                        "ROLE_" + role
                                )
                );
    }

}
