package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.drive.DriveResponse;
import com.campushire.service.DriveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drives")
@RequiredArgsConstructor
@Tag(name = "Hiring Drives", description = "Public hiring drive listing")
public class DriveController {

    private final DriveService driveService;

    @GetMapping
    @Operation(summary = "List all published hiring drives", description = "Public endpoint; returns only PUBLISHED drives.")
    public ResponseEntity<ApiResponse<List<DriveResponse>>> listPublished() {
        return ResponseEntity.ok(ApiResponse.success(driveService.listPublished()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a published hiring drive by ID", description = "Public endpoint; DRAFT/CLOSED drives are hidden.")
    public ResponseEntity<ApiResponse<DriveResponse>> getPublished(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(driveService.getPublishedById(id)));
    }
}
