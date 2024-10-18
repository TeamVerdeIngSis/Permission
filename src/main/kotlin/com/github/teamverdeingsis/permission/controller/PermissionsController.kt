package com.github.teamverdeingsis.permission.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/permissions")
class PermissionsController {

    @GetMapping
    fun getAllPermissions(): List<String> {
        return listOf("READ", "WRITE", "DELETE")
    }

    @PostMapping
    fun createPermission(@RequestBody permission: String): String {
        return "Permission $permission created!"
    }
}
