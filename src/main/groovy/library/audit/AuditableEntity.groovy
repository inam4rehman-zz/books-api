package library.audit

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

@MappedSuperclass
@EntityListeners(AuditingEntityListener)
class AuditableEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long id

    @CreatedDate
    @JsonIgnore
    Date createdDate

    @LastModifiedDate
    @JsonIgnore
    Date lastModifiedDate

    @NotNull
    @JsonIgnore
    Boolean isDeleted = Boolean.FALSE
}
