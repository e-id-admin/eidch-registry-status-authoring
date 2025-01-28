/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.shared.model.entity;

import ch.admin.bit.eid.datastore.shared.model.enums.DatastoreStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * A DatastoreEntity is the generic anchor for different files which are managed by this datastore.
 * <p>
 * It allows for unified handling of the most common management actions which we want to perform on our stored data.
 * For example: Deleting or deactivation of entries.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "datastore_entity")
public class DatastoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DatastoreStatusEnum status;
}
