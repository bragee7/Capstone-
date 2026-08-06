package com.campushire.dto.drive;

import com.campushire.model.enums.DriveStatus;
import com.campushire.model.enums.JobType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DriveResponse(
        Long id,
        Long companyId,
        String companyName,
        String title,
        String description,
        JobType jobType,
        String location,
        Double stipend,
        String salaryPackage,
        Double minimumCgpa,
        String eligibleDepartments,
        String requiredSkills,
        LocalDateTime applicationDeadline,
        LocalDate driveDate,
        DriveStatus status,
        long applicationCount,
        LocalDateTime createdAt
) {
}
