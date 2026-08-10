package com.campushire.service;

import com.campushire.dto.auth.LoginRequest;
import com.campushire.dto.auth.RegisterRequest;
import com.campushire.exception.DuplicateResourceException;
import com.campushire.exception.UnauthorizedException;
import com.campushire.model.entity.User;
import com.campushire.model.enums.Role;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.StudentRepository;
import com.campushire.repository.UserRepository;
import com.campushire.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Test
    void register_rejectsDuplicateEmail() {
        AuthService authService = new AuthService(userRepository, studentRepository, companyRepository,
                passwordEncoder, jwtService);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "Password@123", Role.STUDENT);

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    @Test
    void register_rejectsAdminRole() {
        AuthService authService = new AuthService(userRepository, studentRepository, companyRepository,
                passwordEncoder, jwtService);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);
        RegisterRequest request = new RegisterRequest("Admin", "admin@example.com", "Password@123", Role.ADMIN);

        assertThrows(UnauthorizedException.class, () -> authService.register(request));
    }

    @Test
    void login_rejectsInvalidPassword() {
        AuthService authService = new AuthService(userRepository, studentRepository, companyRepository,
                passwordEncoder, jwtService);
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("$2a$10$hash");
        user.setActive(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", user.getPasswordHash())).thenReturn(false);

        assertThrows(UnauthorizedException.class,
                () -> authService.login(new LoginRequest("test@example.com", "wrong")));
    }
}
