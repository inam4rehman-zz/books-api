package library.book

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController {
    private  BookService bookService

    @Autowired
    BookController(BookService bookService){
        this.bookService = bookService
    }

    @GetMapping
    @PreAuthorize("hasAuthority('api.books.list')")
    ResponseEntity list(){
       Iterable<Book> books = bookService.listAll()
        return new ResponseEntity(books, HttpStatus.OK)
    }

    @GetMapping('/{id}')
    @PreAuthorize("hasAuthority('api.books.get')")
    ResponseEntity get(@PathVariable('id') long id) {
        Book book = bookService.get(id)
        return new ResponseEntity(book, HttpStatus.OK)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('api.books.create')")
    ResponseEntity save(@RequestBody Book book) {
        Book newBook = bookService.save(book)
        HttpHeaders headers = new HttpHeaders()
        headers.set(HttpHeaders.LOCATION, "/books/${newBook.id}")
        return new ResponseEntity(newBook, headers, HttpStatus.CREATED)
    }

    @DeleteMapping('/{id}')
    @PreAuthorize("hasAuthority('api.books.delete')")
    ResponseEntity delete(@PathVariable('id') long id) {
        bookService.delete(id)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping('/{id}')
    @PreAuthorize("hasAuthority('api.books.update')")
    ResponseEntity update(@RequestBody Book book, @PathVariable('id') long id) {
        Book updatedBook = bookService.update(book, id)
        return new ResponseEntity(updatedBook, HttpStatus.OK)
    }
}
