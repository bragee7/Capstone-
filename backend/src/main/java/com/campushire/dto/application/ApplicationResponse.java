package com.campushire.dto.application;

import com.campushire.model.enums.ApplicationStatus;
import com.campushire.model.enums.JobType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        ApplicationStatus status,
        LocalDateTime appliedAt,
        String coverMessage,
        String resumeUrl,
        Long driveId,
        String driveTitle,
        JobType driveJobType,
        String driveCompanyName,
        Long studentId,
        String studentName,
        String studentEmail,
        String studentDepartment,
        BigDecimal studentCgpa,
        Integer studentGraduationYear
) {
}
