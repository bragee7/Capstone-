package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.drive.DriveRequest;
import com.campushire.dto.drive.DriveResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.DriveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruiter/drives")
@RequiredArgsConstructor
@Tag(name = "Recruiter Drives", description = "Recruiter hiring drive management endpoints")
public class RecruiterDriveController {

    private final DriveService driveService;

    @GetMapping
    @Operation(summary = "List the current recruiter's hiring drives", description = "Includes DRAFT, PUBLISHED, and CLOSED drives.")
    public ResponseEntity<ApiResponse<List<DriveResponse>>> myDrives(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(driveService.myDrives(principal.getId())));
    }

    @PostMapping
    @Operation(summary = "Create a hiring drive", description = "Creates a drive in DRAFT status owned by the current recruiter's company.")
    public ResponseEntity<ApiResponse<DriveResponse>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody DriveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(driveService.create(principal.getId(), request), "Hiring drive created"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a hiring drive", description = "Owner only. Closed drives cannot be edited.")
    public ResponseEntity<ApiResponse<DriveResponse>> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody DriveRequest request) {
        return ResponseEntity.ok(ApiResponse.success(driveService.update(principal.getId(), id, request)));
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish a hiring drive", description = "Owner only. Requires complete details and a future deadline.")
    public ResponseEntity<ApiResponse<DriveResponse>> publish(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(driveService.publish(principal.getId(), id), "Hiring drive published"));
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Close a hiring drive", description = "Owner only. Closes the drive to further applications.")
    public ResponseEntity<ApiResponse<DriveResponse>> close(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(driveService.close(principal.getId(), id), "Hiring drive closed"));
    }
}
