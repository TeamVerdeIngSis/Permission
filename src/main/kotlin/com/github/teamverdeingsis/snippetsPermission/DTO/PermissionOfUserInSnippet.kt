package com.github.teamverdeingsis.snippetsPermission.DTO

import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import java.util.*

data class PermissionOfUserInSnippet (
    val userId: String,
    val snippetId: UUID,
)
