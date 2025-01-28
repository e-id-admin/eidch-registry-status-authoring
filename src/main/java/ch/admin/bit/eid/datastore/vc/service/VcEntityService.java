/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.vc.service;

import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotFoundException;
import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotReadyException;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.service.DatastoreEntityService;
import ch.admin.bit.eid.datastore.vc.model.dto.VcEntityResponseDto;
import ch.admin.bit.eid.datastore.vc.model.entity.VcEntity;
import ch.admin.bit.eid.datastore.vc.model.enums.VcTypeEnum;
import ch.admin.bit.eid.datastore.vc.model.mapper.VcEntityMapper;
import ch.admin.bit.eid.datastore.vc.repository.VcEntityRepository;
import ch.admin.bit.eid.status_registry.authoring_service.config.StatusRegistryProperties;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class VcEntityService {

    private final DatastoreEntityService datastoreEntityService;
    private final VcEntityRepository datastoreFileEntityRepository;

    public String extractJwtPayload(String encodedVc) throws Exception {
        return new String(Base64.getDecoder().decode(encodedVc.split("\\.")[1]));
    }

    public String extractVcPayload(String encodedVc, VcTypeEnum expectedType) throws Exception {
        switch (expectedType) {
            case TokenStatusListJWT -> {
                return extractJwtPayload(encodedVc);
            }
            default -> throw new NotImplementedException("Not supported vc type detected.");
        }
    }

    public VcEntity getDatastoreFileEntity(UUID id, VcTypeEnum vcType) {
        return this.datastoreFileEntityRepository.findByBase_IdAndVcType(id, vcType).orElseThrow(
                () -> new ResourceNotFoundException(id.toString(), DatastoreEntity.class)
        );
    }

    public VcEntity.VcEntityBuilder buildEmptyVcEntity(StatusRegistryProperties registryProperties, DatastoreEntity base, VcTypeEnum vcType) throws URISyntaxException {
        String extension;
        switch (vcType) {
            case TokenStatusListJWT:
                extension = "jwt";
                break;
            default:
                throw new NotImplementedException("Not supported vc type detected. type: " + vcType);
        }

        return VcEntity.builder()
                .vcType(vcType)
                .base(base)
                .readUri(
                        MessageFormat.format(
                                registryProperties.getDataUrlTemplate(),
                                base.getId(),
                                extension
                        )
                );
    }

    public Map<String, VcEntityResponseDto> getAllDatastoreFileEntity(StatusRegistryProperties registryProperties, UUID id) throws URISyntaxException {
        DatastoreEntity base = this.datastoreEntityService.getDatastoreEntity(id);

        List<VcEntity> presentFiles = this.datastoreFileEntityRepository.findByBase_Id(id);

        List<VcEntity> result = new ArrayList<>(presentFiles);

        for (VcTypeEnum vcType : VcTypeEnum.values()) {
            if (presentFiles.stream().anyMatch(o -> o.getVcType() == vcType)) continue;
            result.add(this.buildEmptyVcEntity(registryProperties, base, vcType).build());
        }
        HashMap<String, VcEntityResponseDto> files = new HashMap<>();
        result.forEach(file -> files.put(file.getVcType().name(), VcEntityMapper.entityToVcEntityResponseDto(file)));
        return files;
    }

    @Transactional
    public void saveDatastoreFileEntity(UUID datastoreEntityId, VcEntity content) throws ResourceNotReadyException {
        DatastoreEntity base = this.datastoreEntityService.getDatastoreEntity(datastoreEntityId);

        this.datastoreEntityService.checkCanEdit(base);

        content.setBase(base);

        Optional<VcEntity> existing =
                this.datastoreFileEntityRepository.findByBase_IdAndVcType(base.getId(), content.getVcType());
        existing.ifPresent(datastoreFileEntity -> content.setId(datastoreFileEntity.getId()));
        this.datastoreFileEntityRepository.save(content);

        this.datastoreEntityService.setActive(base);
    }
}
