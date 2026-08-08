package com.campushire.dto.application;

import com.campushire.model.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Application status update request payload")
public record ApplicationStatusRequest(
        @NotNull(message = "Status is required")
        @Schema(example = "SHORTLISTED", allowableValues = {"APPLIED", "SHORTLISTED", "INTERVIEW", "SELECTED", "REJECTED"})
        ApplicationStatus status
) {
}
