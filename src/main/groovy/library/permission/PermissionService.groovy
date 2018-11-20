package library.permission

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Service
@Transactional
@Validated
class PermissionService {
    private final PermissionRepository permissionRepository

    PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository
    }

    Iterable<Permission> findAllByUser(@NotNull Long userId) {
        permissionRepository.findAllByUserId(userId)
    }
}
