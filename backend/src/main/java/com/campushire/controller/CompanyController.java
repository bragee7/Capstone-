package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.company.CompanyProfileRequest;
import com.campushire.dto.company.CompanyProfileResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Recruiter company profile endpoints")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/me")
    @Operation(summary = "Get the current recruiter's company profile")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> getProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(companyService.getProfile(principal.getId())));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current recruiter's company profile")
    public ResponseEntity<ApiResponse<CompanyProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CompanyProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success(companyService.updateProfile(principal.getId(), request)));
    }
}
