package com.ecommerce.userservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(

        String secret,

        Long expiration

) {

}

