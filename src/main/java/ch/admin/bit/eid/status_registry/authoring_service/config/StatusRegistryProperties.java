/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@ConfigurationProperties(prefix = "status-registry")
@Getter
@Setter
public class StatusRegistryProperties {

    /**
     * The format template to create a data read response for a status list entry.
     * Format specifiers: 
     *   {0} -> The entry ID as UUID
     *   {1} -> The entry extension as string
     */
    @NotEmpty
    private String dataUrlTemplate;

    @NotEmpty
    private URL authoringBaseUrl;

}
