/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.vc.model.mapper;

import ch.admin.bit.eid.datastore.vc.model.dto.VcEntityResponseDto;
import ch.admin.bit.eid.datastore.vc.model.entity.VcEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VcEntityMapper {
    public static VcEntityResponseDto entityToVcEntityResponseDto(VcEntity entity) {
        return VcEntityResponseDto.builder()
                .isConfigured(entity.getRawVc() != null)
                .readUri(entity.getReadUri())
                .build();
    }
}
