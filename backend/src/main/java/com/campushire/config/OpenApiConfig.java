package com.campushire.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI campusHireOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CampusHire API")
                        .description("""
                                Internship & Campus Hiring Platform API.

                                ## Auth
                                Register / login at `/api/v1/auth` to obtain a JWT, then send it as \
                                `Authorization: Bearer <token>` on protected endpoints.

                                ## Roles
                                - `STUDENT` → `/api/v1/students/**`, `/api/v1/drives`, `/api/v1/applications`
                                - `RECRUITER` → `/api/v1/recruiter/**`, `/api/v1/companies/**`
                                - `ADMIN` → `/api/v1/admin/**`
                                """)
                        .version("0.1.0")
                        .contact(new Contact().name("CampusHire Team")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME));
    }
}
