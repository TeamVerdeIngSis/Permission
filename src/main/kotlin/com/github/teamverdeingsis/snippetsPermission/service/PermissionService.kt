package com.github.teamverdeingsis.snippetsPermission.service

import com.github.teamverdeingsis.snippetsPermission.controller.PermissionsController
import com.github.teamverdeingsis.snippetsPermission.model.Permission
import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import com.github.teamverdeingsis.snippetsPermission.repository.PermissionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class PermissionService(
    @Autowired
    private val permissionRepository: PermissionRepository) {

    fun getAllPermissions(): List<Permission> {
        return permissionRepository.findAll()
    }

    fun hola(): String {
        return "Hola"
    }

    fun createPermission(userId: String, snippetId: UUID, permissionType: PermissionType): Permission {
        val existingPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
        if (existingPermission != null) {
            return existingPermission
            //TODO, esta mal esto, deberia tirar alguna excepcion
        }
        val permission = Permission(userId = userId, snippetId = snippetId, permission = permissionType)
        return permissionRepository.save(permission)
    }

    fun getPermissionsByUserId(userId: String): List<Permission> {
        val response = permissionRepository.findByUserId(userId)
        if (response.isEmpty()) {
            throw RuntimeException("User with ID $userId not found")
        }
        return response
    }

    fun getPermissionsBySnippetId(snippetId: UUID): List<Permission> {
        return permissionRepository.findBySnippetId(snippetId)
    }

    fun getPermissionByUserIdAndSnippetId(userId: String, snippetId: UUID): Permission? {
        return permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
    }

    fun checkIfOwner(snippetId: UUID, userId: String): ResponseEntity<String> {
        val permission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId)
            ?: return ResponseEntity.badRequest().body("Snippet with the provided ID doesn't exist or no permission found for the user")

        return if (permission.permission == PermissionType.WRITE) {
            ResponseEntity.ok("User is the owner of the snippet")
        } else {
            ResponseEntity.badRequest().body("User is not the owner of the snippet")
        }
    }


}