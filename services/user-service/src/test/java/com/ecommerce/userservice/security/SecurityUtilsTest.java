package com.ecommerce.userservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecurityUtilsTest {

    @Test
    void shouldReturnCurrentUsername() {


        UsernamePasswordAuthenticationToken authentication =

                new UsernamePasswordAuthenticationToken(
                        "john@test.com",
                        null
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        String email = SecurityUtils.getCurrentUserEmail()
                .getName();

        assertEquals("john@test.com",
                email);

        SecurityContextHolder.clearContext();
    }
}
