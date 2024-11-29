package com.github.teamverdeingsis.snippetsPermission

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class PermissionApplication

fun main(args: Array<String>) {
	runApplication<PermissionApplication>(*args)
}
