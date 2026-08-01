package com.campushire.config;

import com.campushire.model.entity.Company;
import com.campushire.model.entity.Student;
import com.campushire.model.entity.User;
import com.campushire.model.enums.Role;
import com.campushire.repository.CompanyRepository;
import com.campushire.repository.StudentRepository;
import com.campushire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds development-only accounts for local testing:
 * - student@example.com / Password@123 (STUDENT)
 * - recruiter@example.com / Password@123 (RECRUITER)
 * - admin@example.com / Password@123 (ADMIN)
 *
 * These passwords are for LOCAL DEVELOPMENT ONLY and must never be used in production.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String DEV_PASSWORD = "Password@123";

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUser("Admin CampusHire", "admin@example.com", Role.ADMIN, null, null);
        seedUser("John Student", "student@example.com", Role.STUDENT, "21CS123", "Computer Science");
        seedUser("Recruiter TechCorp", "recruiter@example.com", Role.RECRUITER, null, null);

        if (userRepository.count() > 0) {
            log.info("Development seed data verified (accounts: admin@example.com, student@example.com, recruiter@example.com).");
        }
    }

    private void seedUser(String name, String email, Role role, String registrationNumber, String department) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .passwordHash(passwordEncoder.encode(DEV_PASSWORD))
                .role(role)
                .phone("")
                .active(true)
                .build();

        if (role == Role.STUDENT) {
            Student student = Student.builder()
                    .user(user)
                    .registrationNumber(registrationNumber)
                    .department(department)
                    .college("Example Engineering College")
                    .graduationYear(2027)
                    .cgpa(BigDecimal.valueOf(8.5))
                    .skills("Java, SQL, Spring Boot")
                    .bio("Development-only seeded student profile.")
                    .build();
            user.setStudent(student);
        } else if (role == Role.RECRUITER) {
            Company company = Company.builder()
                    .user(user)
                    .companyName("TechCorp Solutions")
                    .description("Development-only seeded company.")
                    .industry("Information Technology")
                    .website("https://techcorp.example.com")
                    .location("Bengaluru, India")
                    .verified(false)
                    .build();
            user.setCompany(company);
        }

        userRepository.save(user);
    }
}
