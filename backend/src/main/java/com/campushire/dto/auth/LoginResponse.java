package com.campushire.dto.auth;

public record LoginResponse(
        String token,
        UserResponse user
) {
}
