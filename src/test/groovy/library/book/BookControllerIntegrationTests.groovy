package library.book

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTests {
    @Autowired
    TestRestTemplate template
    @Autowired
    BookService bookService
    Book book

    @Before
    void setup(){
        book = new Book("new book added","author",new Date(System.currentTimeMillis()))
    }


    @Test
    void 'validate Access Denied at GET List - Without Basic Auth'() {
        ResponseEntity<String> responseEntity = template.getForEntity("/books", String)
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.statusCode)
    }

    @Test
    void 'validate Access Granted at GET List - with Valid Basic Auth'() throws Exception {
        ResponseEntity<Iterable> responseEntity = template.withBasicAuth("standard", "standard")
                .getForEntity("/books", Iterable)
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode())
        Assert.assertNotNull(responseEntity.body.size())
    }

    @Test
    void 'validate Access Granted at GET one - with Standard user credentials'() throws Exception {
        ResponseEntity<Book> responseEntity = template.withBasicAuth("standard", "standard")
                .getForEntity("/books/1", Book)

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode())
        Assert.assertEquals(1, responseEntity.body.id)
        Assert.assertEquals("Java Beginner’s Guide", responseEntity.body.title)
    }

    @Test
    void 'validate Access Granted at GET one - with Super user credentials'() throws Exception {
        ResponseEntity<Book> responseEntity = template.withBasicAuth("super", "super")
                .getForEntity("/books/1", Book)

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode())
        Assert.assertEquals(1, responseEntity.body.id)
        Assert.assertEquals("Java Beginner’s Guide", responseEntity.body.title)
    }

    @Test
    void 'validate Access Denied at POST - with Standard user credentials'() throws Exception {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<Book> httpEntity = new HttpEntity<Book>(book, headers)
        ResponseEntity<Book> responseEntity = template.withBasicAuth("standard", "standard")
                .exchange("/books", HttpMethod.POST, httpEntity, Book)

        Assert.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode())
    }

    @Test
    void 'validate Access Granted at POST - with Super user credentials'() throws Exception {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<Book> httpEntity = new HttpEntity<Book>(book, headers)
        ResponseEntity<Book> responseEntity = template.withBasicAuth("super", "super")
                    .exchange("/books", HttpMethod.POST, httpEntity, Book)

        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode())
        Assert.assertEquals(book.title, responseEntity.body.title)
        Assert.assertEquals(book.author, responseEntity.body.author)
        Assert.assertNotNull(responseEntity.body.id)
    }

    @Test
    void 'validate Access Denied at PUT - with Standard user credentials'() throws Exception {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<Book> httpEntity = new HttpEntity<Book>(book, headers)
        ResponseEntity<Book> responseEntity = template.withBasicAuth("standard", "standard")
                .exchange("/books/1", HttpMethod.PUT, httpEntity, Book)

        Assert.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode())
    }

    @Test
    void 'validate Access Granted at PUT - with Super user credentials'() throws Exception {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        book.title = "updated title"
        bookService.save(book)

        HttpEntity<Book> httpEntity = new HttpEntity<Book>(book, headers)
        ResponseEntity<Book> responseEntityPut = template.withBasicAuth("super", "super")
                .exchange("/books/"+book.id, HttpMethod.PUT, httpEntity, Book)

        Assert.assertEquals(HttpStatus.OK, responseEntityPut.getStatusCode())
        Assert.assertEquals("updated title", responseEntityPut.body.title)
        Assert.assertEquals(book.author, responseEntityPut.body.author)
        Assert.assertEquals(book.id, responseEntityPut.body.id)
    }

    @Test
    void 'validate Access Denied at DELETE - with Standard user credentials'() throws Exception {
        ResponseEntity<String> responseEntity = template.withBasicAuth("standard", "standard")
                .exchange("/books/2", HttpMethod.DELETE, null, String)

        Assert.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode())
    }

    @Test
    void 'validate Access Granted at DELETE - with Super user credentials'() throws Exception {
        ResponseEntity<String> responseEntityDelete = template.withBasicAuth("super", "super")
                .exchange("/books/2", HttpMethod.DELETE, null, String)

        Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntityDelete.getStatusCode())
    }
}
