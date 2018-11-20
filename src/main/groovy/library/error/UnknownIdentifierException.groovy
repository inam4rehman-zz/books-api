package library.error

import org.springframework.http.HttpStatus


class UnknownIdentifierException extends RuntimeException{
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND
    private static final String DEFAULT_MESSAGE = 'Unknown record identifier provided'

    UnknownIdentifierException() {
        this(DEFAULT_MESSAGE)
    }

    UnknownIdentifierException(String message) {
        super(message)
    }

    HttpStatus getHttpStatus() {
        return HTTP_STATUS
    }
}
