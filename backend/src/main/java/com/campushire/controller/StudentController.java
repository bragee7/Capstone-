package com.campushire.controller;

import com.campushire.dto.ApiResponse;
import com.campushire.dto.student.StudentProfileRequest;
import com.campushire.dto.student.StudentProfileResponse;
import com.campushire.security.UserPrincipal;
import com.campushire.service.StudentService;
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
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student profile endpoints")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    @Operation(summary = "Get the current student's profile", description = "Includes a profile completion percentage.")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getProfile(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getProfile(principal.getId())));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current student's profile", description = "Partial update: only non-null fields are applied.")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success(studentService.updateProfile(principal.getId(), request)));
    }
}
