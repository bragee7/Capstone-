package com.campushire.service;

import com.campushire.dto.company.CompanyProfileRequest;
import com.campushire.dto.company.CompanyProfileResponse;
import com.campushire.exception.ResourceNotFoundException;
import com.campushire.model.entity.Company;
import com.campushire.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public CompanyProfileResponse getProfile(Long userId) {
        return toResponse(findByUserId(userId));
    }

    @Transactional
    public CompanyProfileResponse updateProfile(Long userId, CompanyProfileRequest request) {
        Company company = findByUserId(userId);

        if (request.companyName() != null && !request.companyName().trim().isEmpty()) {
            company.setCompanyName(request.companyName().trim());
        }
        if (request.description() != null) {
            company.setDescription(request.description().trim());
        }
        if (request.industry() != null) {
            company.setIndustry(request.industry().trim());
        }
        if (request.website() != null) {
            company.setWebsite(request.website().trim());
        }
        if (request.location() != null) {
            company.setLocation(request.location().trim());
        }

        return toResponse(companyRepository.save(company));
    }

    private Company findByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found for the current user."));
    }

    private CompanyProfileResponse toResponse(Company company) {
        return new CompanyProfileResponse(
                company.getId(),
                company.getCompanyName(),
                company.getDescription(),
                company.getIndustry(),
                company.getWebsite(),
                company.getLocation(),
                company.isVerified());
    }
}
