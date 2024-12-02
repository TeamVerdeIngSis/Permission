package com.github.teamverdeingsis.snippetsPermission.dto

import java.util.UUID

data class PermissionOfUserInSnippet(
    val userId: String,
    val snippetId: UUID,
)
