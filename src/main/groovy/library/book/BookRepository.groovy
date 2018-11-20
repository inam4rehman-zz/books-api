package library.book

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface BookRepository extends CrudRepository<Book, Long> {
    @Query('FROM Book b WHERE b.id = ?1 AND b.isDeleted = false')
    Book findOne(Long id)

    @Query('FROM Book b WHERE b.isDeleted = false')
    Iterable<Book> findAll()
}