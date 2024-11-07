package com.github.teamverdeingsis.snippetsPermission.controller

import CreatePermissionRequest
import com.github.teamverdeingsis.snippetsPermission.DTO.PermissionOfUserInSnippet
import com.github.teamverdeingsis.snippetsPermission.model.Permission
import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import com.github.teamverdeingsis.snippetsPermission.service.PermissionService
import jakarta.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/permissions")
class PermissionsController(
    @Autowired
    private val permissionService: PermissionService) {

    @GetMapping
    fun getAllPermissions(): List<Permission> {
        return permissionService.getAllPermissions()
    }

    @GetMapping("/helloNga")
    fun hello(): String {
        return "Hello, World!"
    }

    @PostMapping("/create")
    fun createPermission(@RequestBody createPermissionRequest: CreatePermissionRequest): Permission {
        return permissionService.createPermission(createPermissionRequest.userId, createPermissionRequest.snippetId, createPermissionRequest.permission)
    }

    @GetMapping("/user/{userId}")
    fun getPermissionsByUserId(@PathVariable userId: String): List<Permission> {
        return permissionService.getPermissionsByUserId(userId)
    }

    @GetMapping("/snippet/{snippetId}")
    fun getPermissionsBySnippetId(@PathVariable snippetId: UUID): List<Permission> {
        return permissionService.getPermissionsBySnippetId(snippetId)
    }

    @GetMapping("/permission")
    fun getPermissionByUserIdAndSnippetId(@RequestBody permissionOfUserInSnippet: PermissionOfUserInSnippet): Permission? {
        return permissionService.getPermissionByUserIdAndSnippetId(permissionOfUserInSnippet.userId, permissionOfUserInSnippet.snippetId)
    }

    @PostMapping("/share")
    fun shareSnippet(@RequestBody permissionOfUserInSnippet: PermissionOfUserInSnippet): Permission {
        val permission = PermissionType.READ
        return permissionService.createPermission(permissionOfUserInSnippet.userId, permissionOfUserInSnippet.snippetId, permission)
    }
}