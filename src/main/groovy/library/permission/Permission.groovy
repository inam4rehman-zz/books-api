package library.permission

import javax.persistence.Entity
import javax.validation.constraints.NotNull

import library.audit.AuditableEntity

@Entity
class Permission extends AuditableEntity {
    @NotNull
    String name

    String description

    @NotNull
    Boolean isImmutable = Boolean.FALSE
}
