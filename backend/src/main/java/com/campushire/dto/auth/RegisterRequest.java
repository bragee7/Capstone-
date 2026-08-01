package com.campushire.dto.auth;

import com.campushire.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Registration request payload")
public record RegisterRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must be at most 120 characters")
        @Schema(example = "John Student")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 160, message = "Email must be at most 160 characters")
        @Schema(example = "student@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 60, message = "Password must be between 8 and 60 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#])[A-Za-z\\d@$!%*?&.#]{8,60}$",
                message = "Password must contain uppercase, lowercase, a digit, and a special character"
        )
        @Schema(example = "Password@123")
        String password,

        @NotNull(message = "Role is required")
        @Schema(example = "STUDENT", allowableValues = {"STUDENT", "RECRUITER"})
        Role role
) {
}
