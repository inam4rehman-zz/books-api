package library.book


import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import library.audit.AuditableEntity
import library.util.CustomDateDeserializer
import library.util.CustomDateSerializer

import javax.persistence.Entity
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Book extends AuditableEntity  {
    @NotBlank
    String title

    @NotBlank
    String author

    @NotNull
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    Date publishedDate

    Book(){}
    
    Book(String title, String author, Date publishedDate) {
        this.title = title
        this.author = author
        this.publishedDate = publishedDate
    }
}
