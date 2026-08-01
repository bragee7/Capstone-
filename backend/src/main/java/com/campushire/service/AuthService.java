package com.campushire.service;

import com.campushire.dto.auth.LoginRequest;
import com.campushire.dto.auth.LoginResponse;
import com.campushire.dto.auth.RegisterRequest;
import com.campushire.dto.auth.UserResponse;
import com.campushire.exception.DuplicateResourceException;
import com.campushire.exception.UnauthorizedException;
import com.campushire.model.entity.Company;
import com.campushire.model.entity.Student;
import com.campushire.model.entity.User;
import com.campushire.model.enums.Role;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.StudentRepository;
import com.campushire.repository.UserRepository;
import com.campushire.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email().toLowerCase())) {
            throw new DuplicateResourceException("An account with email " + request.email() + " already exists.");
        }
        if (request.role() == Role.ADMIN) {
            throw new UnauthorizedException("Self-registration with the ADMIN role is not allowed.");
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(request.email().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(request.role())
                .active(true)
                .build();

        if (request.role() == Role.STUDENT) {
            Student student = Student.builder().user(user).build();
            user.setStudent(student);
        } else if (request.role() == Role.RECRUITER) {
            Company company = Company.builder().user(user).companyName(user.getName()).build();
            user.setCompany(company);
        }

        userRepository.save(user);
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().toLowerCase().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password.");
        }
        if (!user.isActive()) {
            throw new UnauthorizedException("Your account has been deactivated. Contact the administrator.");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, toUserResponse(user));
    }

    @Transactional(readOnly = true)
    public UserResponse me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found."));
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.isActive());
    }
}
