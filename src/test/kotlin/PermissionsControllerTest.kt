import com.github.teamverdeingsis.snippetsPermission.controller.PermissionsController
import com.github.teamverdeingsis.snippetsPermission.model.Permission
import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import com.github.teamverdeingsis.snippetsPermission.service.PermissionService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(PermissionsController::class)
@ContextConfiguration(classes = [PermissionsController::class]) // Agregado para definir el contexto manualmente
class PermissionsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var permissionService: PermissionService

    @Test
    fun `test getAllPermissions returns list of permissions`() {
        val permissions = listOf(
            Permission("user1", UUID.randomUUID(), PermissionType.READ),
            Permission("user2", UUID.randomUUID(), PermissionType.WRITE),
        )

        `when`(permissionService.getAllPermissions()).thenReturn(permissions)

        mockMvc.perform(get("/api/permissions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(permissions.size))
    }

    @Test
    fun `test hello endpoint`() {
        `when`(permissionService.hola()).thenReturn("Hola")

        mockMvc.perform(get("/api/permissions/helloNga"))
            .andExpect(status().isOk)
            .andExpect(content().string("Hola"))
    }

    @Test
    fun `test createPermission creates and returns permission`() {
        val request = """
            {
              "userId": "user1",
              "snippetId": "${UUID.randomUUID()}",
              "permission": "READ"
            }
        """.trimIndent()

        val permission = Permission("user1", UUID.randomUUID(), PermissionType.READ)

        `when`(permissionService.createPermission(any(), any(), any())).thenReturn(permission)

        mockMvc.perform(
            post("/api/permissions/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(permission.userId))
    }

    @Test
    fun `test getPermissionsByUserId returns permissions`() {
        val permissions = listOf(
            Permission("user1", UUID.randomUUID(), PermissionType.READ),
        )

        `when`(permissionService.getPermissionsByUserId("user1")).thenReturn(permissions)

        mockMvc.perform(get("/api/permissions/user/user1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(permissions.size))
    }

    @Test
    fun `test getPermissionsBySnippetId returns permissions`() {
        val permissions = listOf(
            Permission("user1", UUID.randomUUID(), PermissionType.READ),
        )

        `when`(permissionService.getPermissionsBySnippetId(any())).thenReturn(permissions)

        mockMvc.perform(get("/api/permissions/snippet/${UUID.randomUUID()}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(permissions.size))
    }

    @Test
    fun `test shareSnippet creates and returns permission`() {
        val permission = Permission("user1", UUID.randomUUID(), PermissionType.READ)
        val request = """
            {
              "userId": "user1",
              "snippetId": "${UUID.randomUUID()}"
            }
        """.trimIndent()

        `when`(permissionService.createPermission(any(), any(), any())).thenReturn(permission)

        mockMvc.perform(
            post("/api/permissions/share")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value(permission.userId))
    }

    @Test
    fun `test checkOwner returns success`() {
        val request = """
            {
              "userId": "user1",
              "snippetId": "${UUID.randomUUID()}"
            }
        """.trimIndent()

        `when`(permissionService.checkIfOwner(any(), any())).thenReturn(ResponseEntity.ok("User is the owner of the snippet"))

        mockMvc.perform(
            post("/api/permissions/check-owner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request),
        )
            .andExpect(status().isOk)
            .andExpect(content().string("User is the owner of the snippet"))
    }
}
