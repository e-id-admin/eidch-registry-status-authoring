/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.config;

import ch.admin.bit.eid.StatusRegistryAuthoringServiceApplication;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Status Registry Authoring Service",
                description = "Fill with a documentation",
                contact = @Contact(
                        email = "eid@bit.admin.ch",
                        name = "eID",
                        url = "https://confluence.bit.admin.ch/display/EIDTEAM"
                )
        ),
        externalDocs = @ExternalDocumentation(
                url = "https://confluence.bit.admin.ch/display/EIDTEAM/Datastore/Authoring-Service"
        ),
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

    @Bean
    GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch("/api/**")
                .packagesToScan(StatusRegistryAuthoringServiceApplication.class.getPackageName())
                .build();
    }
}
