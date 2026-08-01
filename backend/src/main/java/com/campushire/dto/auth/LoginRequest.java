package com.campushire.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request payload")
public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(example = "student@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Schema(example = "Password@123")
        String password
) {
}
