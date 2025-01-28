/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.vc.repository;

import ch.admin.bit.eid.datastore.vc.model.entity.VcEntity;
import ch.admin.bit.eid.datastore.vc.model.enums.VcTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VcEntityRepository extends JpaRepository<VcEntity, Long> {
    List<VcEntity> findByBase_Id(UUID baseId);

    Optional<VcEntity> findByBase_IdAndVcType(UUID baseId, VcTypeEnum vcType);
}
