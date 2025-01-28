/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatastoreStatusEnum {
    ACTIVE("ACTIVE"),
    DEACTIVATED("DEACTIVATED"),
    DISABLED("DISABLED"),
    SETUP("SETUP");

    private final String value;
}
