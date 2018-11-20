package library.book

import library.error.UnknownIdentifierException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import javax.validation.Valid
import javax.validation.constraints.NotNull

@Service
class BookService {
    private final BookRepository bookRepository

    @Autowired
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository
    }

    void delete(@NotNull Long id) {
        Book book = get(id)
        book.isDeleted = true
        bookRepository.save(book)
    }

    Book get(@NotNull Long id) {
        Book book = bookRepository.findOne(id)
        if (!book) {
            throw new UnknownIdentifierException()
        }
        return book
    }

    Iterable<Book> listAll() {
        return bookRepository.findAll()
    }

    Book save(@Valid Book bookIn) {
        return bookRepository.save(bookIn)
    }

    Book update(@Valid Book bookIn, @NotNull Long id){
        Book existingBook = get(id)
        existingBook.with {
            title = bookIn.title
            author = bookIn.author
            publishedDate = bookIn.publishedDate
            isDeleted = bookIn.isDeleted
        }
        return bookRepository.save(existingBook)
    }

}
