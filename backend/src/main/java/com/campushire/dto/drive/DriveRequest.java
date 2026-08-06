package com.campushire.dto.drive;

import com.campushire.model.enums.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Hiring drive create/update request payload")
public record DriveRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must be at most 200 characters")
        @Schema(example = "Summer Internship 2026")
        String title,

        @Size(max = 10000, message = "Description must be at most 10000 characters")
        @Schema(example = "Hands-on internship for final year students.")
        String description,

        @Schema(example = "INTERNSHIP", allowableValues = {"INTERNSHIP", "FULL_TIME", "PART_TIME", "CONTRACT"})
        JobType jobType,

        @Size(max = 160, message = "Location must be at most 160 characters")
        @Schema(example = "Hyderabad, India")
        String location,

        @Schema(example = "25000")
        Double stipend,

        @Size(max = 60, message = "Salary package must be at most 60 characters")
        @Schema(example = "12 LPA")
        String salaryPackage,

        @DecimalMin(value = "0.0", message = "Minimum CGPA must be at least 0.0")
        @DecimalMax(value = "10.0", message = "Minimum CGPA must be at most 10.0")
        @Schema(example = "7.0")
        Double minimumCgpa,

        @Size(max = 500, message = "Eligible departments must be at most 500 characters")
        @Schema(example = "Computer Science, Information Technology")
        String eligibleDepartments,

        @Size(max = 500, message = "Required skills must be at most 500 characters")
        @Schema(example = "Java, Spring Boot, SQL")
        String requiredSkills,

        @NotNull(message = "Application deadline is required")
        @Schema(example = "2026-09-15T23:59:59")
        LocalDateTime applicationDeadline,

        @Schema(example = "2026-09-20")
        LocalDate driveDate
) {
}
