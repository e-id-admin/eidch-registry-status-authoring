/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.status_registry.authoring_service.controller;

import ch.admin.bit.eid.datastore.shared.exceptions.ApiError;
import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityResponseDto;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.model.mapper.DatastoreEntityMapper;
import ch.admin.bit.eid.datastore.shared.service.DatastoreEntityService;
import ch.admin.bit.eid.datastore.vc.model.enums.VcTypeEnum;
import ch.admin.bit.eid.datastore.vc.service.VcEntityService;
import ch.admin.bit.eid.status_registry.authoring_service.config.StatusRegistryProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statuslist")
@AllArgsConstructor
@Tag(name = "VC Controller", description = "Manages VC entries in the datastore.")
public class StatusListController {

    private final DatastoreEntityService datastoreEntityService;
    private final VcEntityService vcEntityService;
    private final StatusRegistryProperties statusRegistryProperties;

    @PutMapping(value = "/{datastoreEntryId}.jwt")
    @Operation(
            summary = "Update a token statuslist vc entry in the datastore.",
            description = "Update a token statuslist vc entry in the datastore."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = true),
                    @ApiResponse(
                            responseCode = "425",
                            description = "Too Early, Resource cannot be edited.",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
            }
    )
    public DatastoreEntityResponseDto updateVcEntry(@Valid @PathVariable UUID datastoreEntryId, @RequestBody String body)
            throws Exception {
        DatastoreEntity datastoreEntity = this.datastoreEntityService.getDatastoreEntity(datastoreEntryId);

        var vcPayload = this.vcEntityService.extractVcPayload(body, VcTypeEnum.TokenStatusListJWT);

        this.vcEntityService.saveDatastoreFileEntity(
                datastoreEntryId,
                this.vcEntityService.buildEmptyVcEntity(
                                this.statusRegistryProperties,
                                datastoreEntity,
                                VcTypeEnum.TokenStatusListJWT
                        )
                        .rawVc(body)
                        .vcPayload(vcPayload)
                        .build()
        );

        return DatastoreEntityMapper.entityToDatastoreEntityResponseDto(
                datastoreEntity,
                this.vcEntityService.getAllDatastoreFileEntity(this.statusRegistryProperties, datastoreEntryId)
        );
    }
}
