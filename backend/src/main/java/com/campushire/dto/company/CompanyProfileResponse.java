package com.campushire.dto.company;

public record CompanyProfileResponse(
        Long id,
        String companyName,
        String description,
        String industry,
        String website,
        String location,
        boolean verified
) {
}
