import com.github.teamverdeingsis.snippetsPermission.model.Permission
import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import com.github.teamverdeingsis.snippetsPermission.repository.PermissionRepository
import com.github.teamverdeingsis.snippetsPermission.service.PermissionService
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class PermissionServiceTest {

    private lateinit var permissionService: PermissionService
    private val permissionRepository: PermissionRepository = mock()

    @BeforeEach
    fun setUp() {
        permissionService = PermissionService(permissionRepository)
    }

    @Test
    fun `test getAllPermissions returns all permissions`() {
        val permissions = listOf(
            Permission(userId = "user1", snippetId = UUID.randomUUID(), permission = PermissionType.READ),
            Permission(userId = "user2", snippetId = UUID.randomUUID(), permission = PermissionType.WRITE)
        )

        whenever(permissionRepository.findAll()).thenReturn(permissions)

        val result = permissionService.getAllPermissions()

        assertEquals(2, result.size)
        assertEquals("user1", result[0].userId)
        assertEquals("user2", result[1].userId)
    }

    @Test
    fun `test createPermission returns existing permission if it already exists`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permissionType = PermissionType.READ
        val existingPermission = Permission(userId, snippetId, permissionType)

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(existingPermission)

        val result = permissionService.createPermission(userId, snippetId, permissionType)

        assertEquals(existingPermission, result)
    }

    @Test
    fun `test getPermissionsByUserId returns permissions for user`() {
        val userId = "user1"
        val permissions = listOf(
            Permission(userId = userId, snippetId = UUID.randomUUID(), permission = PermissionType.READ),
            Permission(userId = userId, snippetId = UUID.randomUUID(), permission = PermissionType.WRITE)
        )

        whenever(permissionRepository.findByUserId(userId)).thenReturn(permissions)

        val result = permissionService.getPermissionsByUserId(userId)

        assertEquals(2, result.size)
        assertEquals(userId, result[0].userId)
    }

    @Test
    fun `test getPermissionsByUserId throws exception if no permissions found`() {
        val userId = "user1"

        whenever(permissionRepository.findByUserId(userId)).thenReturn(emptyList())

        val exception = assertThrows<RuntimeException> {
            permissionService.getPermissionsByUserId(userId)
        }

        assertEquals("User with ID $userId not found", exception.message)
    }

    @Test
    fun `test getPermissionsBySnippetId returns permissions for snippet`() {
        val snippetId = UUID.randomUUID()
        val permissions = listOf(
            Permission(userId = "user1", snippetId = snippetId, permission = PermissionType.READ),
            Permission(userId = "user2", snippetId = snippetId, permission = PermissionType.WRITE)
        )

        whenever(permissionRepository.findBySnippetId(snippetId)).thenReturn(permissions)

        val result = permissionService.getPermissionsBySnippetId(snippetId)

        assertEquals(2, result.size)
        assertEquals(snippetId, result[0].snippetId)
    }

    @Test
    fun `test getPermissionByUserIdAndSnippetId returns permission if exists`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permission = Permission(userId, snippetId, PermissionType.READ)

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(permission)

        val result = permissionService.getPermissionByUserIdAndSnippetId(userId, snippetId)

        assertEquals(permission, result)
    }

    @Test
    fun `test getPermissionByUserIdAndSnippetId returns null if not found`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null)

        val result = permissionService.getPermissionByUserIdAndSnippetId(userId, snippetId)

        assertNull(result)
    }

    @Test
    fun `test checkIfOwner returns success if user is owner`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permission = Permission(userId, snippetId, PermissionType.WRITE)

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(permission)

        val result = permissionService.checkIfOwner(snippetId, userId)

        assertEquals(
            "User is the owner of the snippet",
            result.body
        )
    }

    @Test
    fun `test checkIfOwner returns error if user is not owner`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()
        val permission = Permission(userId, snippetId, PermissionType.READ)

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(permission)

        val result = permissionService.checkIfOwner(snippetId, userId)

        assertEquals(
            "User is not the owner of the snippet",
            result.body
        )
    }

    @Test
    fun `test checkIfOwner returns error if permission not found`() {
        val userId = "user1"
        val snippetId = UUID.randomUUID()

        whenever(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null)

        val result = permissionService.checkIfOwner(snippetId, userId)

        assertEquals(
            "Snippet with the provided ID doesn't exist or no permission found for the user",
            result.body
        )
    }
}

