/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.controller;

import ch.admin.bit.eid.datastore.shared.exceptions.ApiError;
import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityRequestDto;
import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityResponseDto;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.model.mapper.DatastoreEntityMapper;
import ch.admin.bit.eid.datastore.shared.service.DatastoreEntityService;
import ch.admin.bit.eid.datastore.vc.service.VcEntityService;
import ch.admin.bit.eid.status_registry.authoring_service.config.StatusRegistryProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/entry")
@AllArgsConstructor
@Tag(name = "Datastore", description = "Manages entries in the Datastore.")
public class DatastoreEntityController {

    private final DatastoreEntityService datastoreEntityService;
    private final VcEntityService vcEntityService;
    private final StatusRegistryProperties statusRegistryProperties;

    @GetMapping(value = "/{datastoreEntryId}")
    @Operation(
            summary = "Get an entry configuration from the Datastore.",
            description = "Get an entry configuration from the Datastore."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = true),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
            }
    )
    public DatastoreEntityResponseDto getDatastoreEntry(@Valid @PathVariable(name = "datastoreEntryId") UUID datastoreEntryId) throws URISyntaxException {
        DatastoreEntity datastoreEntity = this.datastoreEntityService.getDatastoreEntity(datastoreEntryId);

        var files = this.vcEntityService.getAllDatastoreFileEntity(this.statusRegistryProperties, datastoreEntryId);

        return DatastoreEntityMapper.entityToDatastoreEntityResponseDto(datastoreEntity, files);
    }

    @PostMapping(value = "/")
    @Operation(summary = "Create a new entry in the Datastore.", description = "Create a new entry in the Datastore.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Success", useReturnTypeSchema = true)})
    public DatastoreEntityResponseDto createDatastoreEntry(HttpServletResponse response) throws URISyntaxException {
        DatastoreEntity datastoreEntity = this.datastoreEntityService.createEmptyDatastoreEntity();

        response.setStatus(HttpServletResponse.SC_CREATED);

        return DatastoreEntityMapper.entityToDatastoreEntityResponseDto(
                datastoreEntity,
                this.vcEntityService.getAllDatastoreFileEntity(this.statusRegistryProperties, datastoreEntity.getId())
        );
    }

    @PatchMapping(value = "/{datastoreEntryId}")
    @Operation(
            summary = "Update an entry configuration from the Datastore.",
            description = "Update an entry configuration from the Datastore."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = true),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
            }
    )
    public DatastoreEntityResponseDto updateDatastoreEntry(
            @Valid @PathVariable(name = "datastoreEntryId") UUID datastoreEntryId,
            @RequestBody DatastoreEntityRequestDto body
    ) throws URISyntaxException {
        this.datastoreEntityService.updateDatastoreEntity(datastoreEntryId, body);
        return this.getDatastoreEntry(datastoreEntryId);
    }
}
