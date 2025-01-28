/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.model.dto;

import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DatastoreEntityRequestDto {

    private Optional<DatastoreStatusEnum> status;
}
