package library.book

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import library.auth.PermissionBasedUserDetailsService
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import java.text.SimpleDateFormat

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
class BookControllerUnitTests {
    @Autowired
    private MockMvc mvc
    @MockBean
    BookService bookService
    @MockBean
    PermissionBasedUserDetailsService userDetailsService
    ObjectMapper objectMapper = new ObjectMapper()
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS'Z'")
    Date date = new Date(System.currentTimeMillis())

    Book book1 = new Book("book 1", "author", date)
    Book book2 = new Book("book 2", "an other author", date)
    Iterable<Book> books = Arrays.asList(book1, book2)

    @Test
    void 'validate JSON response from LIST method'() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // mock the return from bookService
        when(bookService.listAll()).thenReturn(books)

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books")
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$", IsCollectionWithSize.hasSize(2)))
        // book1
                .andExpect(jsonPath("\$[0]['title']").value(book1.title))
                .andExpect(jsonPath("\$[0]['author']").value(book1.author))
                .andExpect(jsonPath("\$[0]['publishedDate']")
                .value(sdf.format(book1.publishedDate).toString()))
        // book2
                .andExpect(jsonPath("\$[1]['title']").value(book2.title))
                .andExpect(jsonPath("\$[1]['author']").value(book2.author))
                .andExpect(jsonPath("\$[1]['publishedDate']")
                .value(sdf.format(book2.publishedDate).toString()))
    }

    @Test
    void 'validate JSON response from GET method'() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        book1.setId(1)
        when(bookService.get(1)).thenReturn(book1)

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books/1")
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$['title']").value(book1.title))
                .andExpect(jsonPath("\$['author']").value(book1.author))
                .andExpect(jsonPath("\$['publishedDate']")
                .value(sdf.format(book1.publishedDate).toString()))
    }

    @Test
    void 'validate JSON response from SAVE method'() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        Book newBook = new Book("new book", "new author", date)
        when(bookService.save(any() as Book)).thenReturn(newBook)

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter()
        String requestJson = objectWriter.writeValueAsString(newBook)

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .accept(MediaType.APPLICATION_JSON).content(requestJson).characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        newBook.setId(3)
        mvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/books/" + newBook.id))
                .andExpect(redirectedUrl("/books/" + newBook.id))
    }

    @Test
    void 'validate JSON response from PUT method'() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        book1.title = "updated tile"
        book1.author = "updated author"
        book1.setId(1)
        when(bookService.update(any() as Book, any() as Long)).thenReturn(book1)

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter()
        String requestJson = objectWriter.writeValueAsString(book1)

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/" + book1.id)
                .accept(MediaType.APPLICATION_JSON).content(requestJson).characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$['id']").value(book1.id))
                .andExpect(jsonPath("\$['title']").value(book1.title))
                .andExpect(jsonPath("\$['author']").value(book1.author))
                .andExpect(jsonPath("\$['publishedDate']")
                .value(sdf.format(book1.publishedDate).toString()))
    }

    @Test
    void 'validate JSON response from DELETE method'() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        book1.setId(1)
        book1.isDeleted = true
        doNothing().when(bookService).delete(any() as Long)

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter()
        String requestJson = objectWriter.writeValueAsString(book1)

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/" + book1.id)
                .accept(MediaType.APPLICATION_JSON).content(requestJson).characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        mvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
    }
}