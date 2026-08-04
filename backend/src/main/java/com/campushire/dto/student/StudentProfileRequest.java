package com.campushire.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Student profile update request payload")
public record StudentProfileRequest(
        @Size(max = 40, message = "Registration number must be at most 40 characters")
        @Schema(example = "21CS123")
        String registrationNumber,

        @Size(max = 80, message = "Department must be at most 80 characters")
        @Schema(example = "Computer Science")
        String department,

        @Size(max = 120, message = "College must be at most 120 characters")
        @Schema(example = "Example Engineering College")
        String college,

        @Schema(example = "2027")
        Integer graduationYear,

        @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
        @DecimalMax(value = "10.0", message = "CGPA must be at most 10.0")
        @Schema(example = "8.5")
        BigDecimal cgpa,

        @Size(max = 2000, message = "Skills must be at most 2000 characters")
        @Schema(example = "Java, SQL, Spring Boot")
        String skills,

        @Size(max = 500, message = "Resume URL must be at most 500 characters")
        @Schema(example = "https://example.com/resume.pdf")
        String resumeUrl,

        @Size(max = 5000, message = "Bio must be at most 5000 characters")
        @Schema(example = "Final year computer science student.")
        String bio
) {
}
