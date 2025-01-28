/*
 * SPDX-FileCopyrightText: 2025 Swiss Confederation
 *
 * SPDX-License-Identifier: MIT
 */

package ch.admin.bit.eid.datastore.vc.model.entity;

import ch.admin.bit.eid.datastore.shared.model.entity.DatastoreEntity;
import ch.admin.bit.eid.datastore.vc.model.enums.VcTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A VcEntity represents a VC of different formats and types in our data store.
 * <p>
 * VcEntity does store the actual vc (with signature and other addenda)
 * along the payload json.
 * This allows to filter and search for payload driven elements specific to the
 * type of VC handled while also providing a generalized way how to handle VCs of
 * different types and formats.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "vc_entity")
public class VcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", referencedColumnName = "id")
    private DatastoreEntity base;

    @Column(name = "vc_type")
    @Enumerated(EnumType.STRING)
    private VcTypeEnum vcType;

    @Column(name = "raw_vc")
    private String rawVc;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "vc_payload")
    private String vcPayload;

    @Column(name = "read_uri")
    private String readUri;

}
