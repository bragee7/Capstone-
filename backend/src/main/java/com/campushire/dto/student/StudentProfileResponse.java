package com.campushire.dto.student;

import java.math.BigDecimal;

public record StudentProfileResponse(
        Long id,
        String registrationNumber,
        String department,
        String college,
        Integer graduationYear,
        BigDecimal cgpa,
        String skills,
        String resumeUrl,
        String bio,
        int profileCompletion
) {
}
