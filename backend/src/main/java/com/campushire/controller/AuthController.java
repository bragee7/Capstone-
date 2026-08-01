package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.auth.LoginRequest;
import com.campushire.dto.auth.LoginResponse;
import com.campushire.dto.auth.RegisterRequest;
import com.campushire.dto.auth.UserResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, login, and current-user endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new account", description = "Creates a STUDENT or RECRUITER account. Passwords are bcrypt-hashed.")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.register(request), "Registration successful"));
    }

    @PostMapping("/login")
    @Operation(summary = "Log in and receive a JWT", description = "Validates credentials and returns a JWT plus user details.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request), "Login successful"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(authService.me(principal.getId())));
    }
}
