package com.github.teamverdeingsis.snippetsPermission.controller

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

    @PostMapping("/snippetId/")
    fun createPermission(@RequestBody userId: String, snippetId: UUID, permission: PermissionType): Permission {
        return permissionService.createPermission(userId, snippetId, permission)
    }

    @GetMapping("/user/{userId}")
    fun getPermissionsByUserId(@PathVariable userId: String): List<Permission> {
        return permissionService.getPermissionsByUserId(userId)
    }

    @GetMapping("/snippet/{snippetId}")
    fun getPermissionsBySnippetId(@PathVariable snippetId: UUID): List<Permission> {
        return permissionService.getPermissionsBySnippetId(snippetId)
    }

    @GetMapping("/user/{userId}/snippet/{snippetId}")
    fun getPermissionByUserIdAndSnippetId(@PathVariable userId: String, @PathVariable snippetId: UUID): Permission? {
        return permissionService.getPermissionByUserIdAndSnippetId(userId, snippetId)
    }

    @GetMapping("/user/{userId}/snippets")
    fun getSnippetsByUserId(@PathVariable userId: String): List<UUID> {
        return permissionService.getSnippetsByUserId(userId)
    }
}