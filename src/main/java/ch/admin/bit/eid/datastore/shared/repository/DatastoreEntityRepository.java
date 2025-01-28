/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.repository;

import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DatastoreEntityRepository extends JpaRepository<DatastoreEntity, UUID> {
}
