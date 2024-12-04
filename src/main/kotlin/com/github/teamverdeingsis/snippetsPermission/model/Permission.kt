package com.github.teamverdeingsis.snippetsPermission.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "permission")
data class Permission(
    @Column(name = "userId", nullable = false)
    val userId: String? = null,
    @Column(name = "snippetId", nullable = false)
    val snippetId: UUID? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    val permission: PermissionType? = PermissionType.READ,
    @Id
    val permissionId: String = UUID.randomUUID().toString(),
)
