package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.application.ApplicationRequest;
import com.campushire.dto.application.ApplicationResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Student application endpoints")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Apply to a hiring drive", description = "Students can apply once per drive while it is PUBLISHED and before the deadline.")
    public ResponseEntity<ApiResponse<ApplicationResponse>> apply(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(applicationService.apply(principal.getId(), request), "Application submitted"));
    }

    @GetMapping("/my")
    @Operation(summary = "List the current student's applications")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> myApplications(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.myApplications(principal.getId())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one of the current student's applications")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplication(principal.getId(), id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Withdraw an application", description = "Only applications still in APPLIED status can be withdrawn.")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        applicationService.withdraw(principal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(null, "Application withdrawn"));
    }
}
