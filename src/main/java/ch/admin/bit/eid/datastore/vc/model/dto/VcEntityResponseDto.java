/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.vc.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VcEntityResponseDto {
    private String readUri;
    private Boolean isConfigured;
}
