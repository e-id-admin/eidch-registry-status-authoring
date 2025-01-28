/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.exceptions;

import java.text.MessageFormat;

public class ResourceNotReadyException extends RuntimeException {

    private static final MessageFormat ERR_MESSAGE = new MessageFormat(
            "Resource with id ''{0}'' is not ready for processing."
    );

    public ResourceNotReadyException(String id, Class<?> clazz) {
        super(ERR_MESSAGE.format(new String[]{id, clazz.getSimpleName()}));
    }
}
