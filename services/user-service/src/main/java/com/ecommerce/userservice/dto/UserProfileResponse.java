package com.ecommerce.userservice.dto;

import java.util.UUID;

public record UserProfileResponse (

        UUID id,

        String email,

        String firstName,

        String lastName
) {

}
