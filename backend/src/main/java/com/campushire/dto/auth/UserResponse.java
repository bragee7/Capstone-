package com.campushire.dto.auth;

import com.campushire.model.enums.Role;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        String phone,
        boolean active
) {
}
