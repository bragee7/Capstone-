package com.campushire.dto.company;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Company profile update request payload")
public record CompanyProfileRequest(
        @Size(max = 160, message = "Company name must be at most 160 characters")
        @Schema(example = "TechCorp Solutions")
        String companyName,

        @Size(max = 5000, message = "Description must be at most 5000 characters")
        @Schema(example = "A leading software company.")
        String description,

        @Size(max = 80, message = "Industry must be at most 80 characters")
        @Schema(example = "Information Technology")
        String industry,

        @Size(max = 200, message = "Website must be at most 200 characters")
        @Schema(example = "https://techcorp.example.com")
        String website,

        @Size(max = 160, message = "Location must be at most 160 characters")
        @Schema(example = "Bengaluru, India")
        String location
) {
}
