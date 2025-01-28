/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.model.dto;

import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import ch.admin.bit.eid.datastore.vc.model.dto.VcEntityResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;


@Data
@Builder
public class DatastoreEntityResponseDto {

    private UUID id;
    private DatastoreStatusEnum status;
    private Map<String, VcEntityResponseDto> files;
}
