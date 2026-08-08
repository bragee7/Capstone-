package com.campushire.dto.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Application submission request payload")
public record ApplicationRequest(
        @NotNull(message = "Drive ID is required")
        @Schema(example = "1")
        Long driveId,

        @Size(max = 5000, message = "Cover message must be at most 5000 characters")
        @Schema(example = "I am excited to apply for this internship.")
        String coverMessage,

        @Size(max = 500, message = "Resume URL must be at most 500 characters")
        @Schema(example = "https://example.com/john-resume.pdf")
        String resumeUrl
) {
}
