/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.model.mapper;

import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityRequestDto;
import ch.admin.bit.eid.datastore.shared.model.dto.DatastoreEntityResponseDto;
import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import ch.admin.bit.eid.datastore.vc.model.dto.VcEntityResponseDto;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;

@UtilityClass
public class DatastoreEntityMapper {

    public static DatastoreEntityResponseDto entityToDatastoreEntityResponseDto(
            DatastoreEntity entity,
            Map<String, VcEntityResponseDto> files
    ) {
        DatastoreEntityResponseDto.DatastoreEntityResponseDtoBuilder builder = DatastoreEntityResponseDto.builder()
                .id(entity.getId())
                .status(entity.getStatus());

        builder.files(files);
        return builder.build();
    }

    public static void dtoToDatastoreEntity(DatastoreEntity target, DatastoreEntityRequestDto source) {
        Optional<DatastoreStatusEnum> status = source.getStatus();
        status.ifPresent(target::setStatus);
    }
}
