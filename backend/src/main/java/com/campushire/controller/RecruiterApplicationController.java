package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.application.ApplicationResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiter/applications")
@RequiredArgsConstructor
@Tag(name = "Recruiter Applications", description = "Cross-drive application listing for recruiters")
public class RecruiterApplicationController {

    private final ApplicationService applicationService;

    @GetMapping
    @Operation(summary = "List all applications across the current recruiter's drives")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> allForRecruiter(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.listAllForRecruiter(principal.getId())));
    }
}
