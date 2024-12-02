package team.github.teamverdeingsis.snippetsPermission.repository

import com.github.teamverdeingsis.snippetsPermission.model.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PermissionRepository : JpaRepository<Permission, UUID> {
    fun findByUserId(userId: String): List<Permission>
    fun findBySnippetId(snippetId: UUID): List<Permission>
    fun findByUserIdAndSnippetId(userId: String, snippetId: UUID): Permission?
    fun findSnippetsByUserId(userId: String): List<Permission>?
}
