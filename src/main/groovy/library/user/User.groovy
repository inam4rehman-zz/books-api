package library.user

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.validation.constraints.NotBlank

import library.audit.AuditableEntity
import library.role.Role

import javax.validation.constraints.NotNull

@Entity
class User extends AuditableEntity {
    @NotBlank
    String firstName

    @NotBlank
    String lastName

    @NotBlank
    String username

    @NotBlank
    String password

    @NotNull
    Boolean isAccountExpired = Boolean.FALSE

    @NotNull
    Boolean isAccountLocked = Boolean.FALSE

    @NotNull
    Boolean isCredentialsExpired = Boolean.FALSE

    @NotNull
    Boolean isEnabled = Boolean.TRUE


    @ManyToMany
    @JoinTable(name='user_role', joinColumns=@JoinColumn(name='user_id'), inverseJoinColumns=@JoinColumn(name='role_id'))
    @JsonIgnore
    Set<Role> roles
}
