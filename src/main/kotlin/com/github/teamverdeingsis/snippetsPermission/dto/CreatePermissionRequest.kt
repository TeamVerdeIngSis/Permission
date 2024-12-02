import com.github.teamverdeingsis.snippetsPermission.model.PermissionType
import java.util.*

data class CreatePermissionRequest(
    val userId: String,
    val snippetId: UUID,
    val permission: PermissionType,
)
