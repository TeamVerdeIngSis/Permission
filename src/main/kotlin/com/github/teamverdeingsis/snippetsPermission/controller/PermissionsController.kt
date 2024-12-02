package com.github.teamverdeingsis.snippetsPermission.controller

import CreatePermissionRequest
import com.github.teamverdeingsis.snippetsPermission.dto.PermissionOfUserInSnippet
import com.github.teamverdeingsis.snippetsPermission.model.Permission
import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import com.github.teamverdeingsis.snippetsPermission.service.PermissionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/permissions")
class PermissionsController(
    @Autowired
    private val permissionService: PermissionService,
) {

    @GetMapping
    fun getAllPermissions(): List<Permission> {
        return permissionService.getAllPermissions()
    }

    private val logger = LoggerFactory.getLogger(PermissionsController::class.java)

    @GetMapping("/helloNga")
    fun hello(): String {
        println("Jijo")
        logger.info("Executing /helloNga endpoint")
        return permissionService.hola()
    }

    @PostMapping("/create")
    fun createPermission(@RequestBody createPermissionRequest: CreatePermissionRequest): Permission {
        return permissionService.createPermission(createPermissionRequest.userId, createPermissionRequest.snippetId, createPermissionRequest.permission)
    }

    @GetMapping("/user/{userId}")
    fun getPermissionsByUserId(@PathVariable userId: String): List<Permission> {
        val response = permissionService.getPermissionsByUserId(userId)
        return response
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

    @PostMapping("/check-owner")
    fun checkOwner(@RequestBody request: CheckOwnerRequest): ResponseEntity<String> {
        return permissionService.checkIfOwner(request.snippetId, request.userId)
    }

    data class CheckOwnerRequest(
        val snippetId: UUID,
        val userId: String,
    )
}
