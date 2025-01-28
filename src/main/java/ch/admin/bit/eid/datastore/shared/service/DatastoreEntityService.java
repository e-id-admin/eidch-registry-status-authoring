/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.service;

import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotFoundException;
import ch.admin.bit.eid.datastore.shared.exceptions.ResourceNotReadyException;
import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityRequestDto;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import ch.admin.bit.eid.datastore.shared.model.mapper.DatastoreEntityMapper;
import ch.admin.bit.eid.datastore.shared.repository.DatastoreEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DatastoreEntityService {

    private final DatastoreEntityRepository datastoreEntityRepository;

    public void checkCanEdit(DatastoreEntity entry) throws ResourceNotReadyException {
        if (entry.getStatus() == DatastoreStatusEnum.DISABLED) throw new ResourceNotReadyException(
                entry.getId().toString(),
                DatastoreEntity.class
        );
    }

    public void checkCanShow(DatastoreEntity entry) throws ResourceNotReadyException {
        this.checkCanEdit(entry);
        if (
                !(entry.getStatus() != DatastoreStatusEnum.SETUP && entry.getStatus() != DatastoreStatusEnum.DEACTIVATED)
        ) throw new ResourceNotReadyException(entry.getId().toString(), DatastoreEntity.class);
    }

    public DatastoreEntity getDatastoreEntity(UUID id) {
        return this.datastoreEntityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id.toString(), DatastoreEntity.class)
        );
    }

    public DatastoreEntity createEmptyDatastoreEntity() {
        return this.datastoreEntityRepository.save(DatastoreEntity.builder().status(DatastoreStatusEnum.SETUP).build());
    }

    public DatastoreEntity updateDatastoreEntity(UUID id, DatastoreEntityRequestDto body) {
        DatastoreEntity entity = this.getDatastoreEntity(id);

        DatastoreEntityMapper.dtoToDatastoreEntity(entity, body);

        return this.datastoreEntityRepository.save(entity);
    }

    public void setActive(DatastoreEntity base) {
        base.setStatus(DatastoreStatusEnum.ACTIVE);
        this.datastoreEntityRepository.save(base);
    }
}
