/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.jwt")
@Getter
@Setter
public class SecurityOAuth2JWTProperties {
    @NotNull
    @NotEmpty
    private Map<String, String> issuerUris = new HashMap<>();


    @Bean
    public JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver() {
        return JwtIssuerAuthenticationManagerResolver.fromTrustedIssuers(getIssuerUris().values());
    }
}
