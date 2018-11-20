package library.book

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import static org.junit.Assert.assertEquals
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class bookServiceUnitTests {
    @Mock
    BookRepository bookRepository
    @InjectMocks
    BookService bookService
    Book book1
    Book book2

    @Before
    void setup(){
        book1 = new Book("test book 1", "author 1", new Date(2018,10,11))
        book2 = new Book("test book 2", "author 2", new Date(2018,10,11))
    }

    @Test
     void "validate findAll method return list of books"() {
        Iterable<Book> booksList = Arrays.asList(book1, book2)
        when(bookRepository.findAll()).thenReturn(booksList)
        Iterable<Book> bookList = bookService.listAll()
        assertEquals(bookList[0].title, book1.title)
        assertEquals(bookList[0].author, book1.author)
        assertEquals(bookList[0].publishedDate, book1.publishedDate)

        assertEquals(bookList[1].title, book2.title)
        assertEquals(bookList[1].author, book2.author)
        assertEquals(bookList[1].publishedDate, book2.publishedDate)

        // verify that findAll() method was called
        verify(bookRepository).findAll()
    }

    @Test
    void "validate get method returns a book"() {
        when(bookRepository.findOne(any(Long.class))).thenReturn(book1)
        Book book = bookService.get(1)
        assertEquals(book.title, book1.title)
        assertEquals(book.author, book1.author)
        assertEquals(book.publishedDate, book1.publishedDate)

        // verify that findOne() method was called
        verify(bookRepository).findOne(any(Long.class))
    }

    @Test
    void "validate save method should save a book"() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1)
        Book book = bookService.save(book1)
        assertEquals(book.title, book1.title)
        assertEquals(book.author, book1.author)
        assertEquals(book.publishedDate, book1.publishedDate)

        // verify that save() method was called
        verify(bookRepository).save(any(Book.class))
    }

    @Test
    void "validate update method should update the book"() {
        when(bookRepository.findOne(any(Long.class))).thenReturn(book1)
        when(bookRepository.save(any(Book.class))).thenReturn(book1)
        Book book = bookService.update(book1,any(Long.class))
        assertEquals(book.title, book1.title)
        assertEquals(book.author, book1.author)
        assertEquals(book.publishedDate, book1.publishedDate)

        // verify that findOne and save methods were called
        verify(bookRepository).findOne(any(Long.class))
        verify(bookRepository).save(any(Book.class))
    }

    @Test
    void "validate delete method should delete a book"() {
        when(bookRepository.findOne(any(Long.class))).thenReturn(book1)
        /* we ony logically delete the book by changing isDeleted property to TRUE
            and then called bookRepository.save
         */
        when(bookRepository.save(any(Book.class))).thenReturn(book1)
        bookService.delete(any(Long.class))
        // verify that findOne and save methods were called
        verify(bookRepository).findOne(any(Long.class))
        verify(bookRepository).save(any(Book.class))
    }
}
