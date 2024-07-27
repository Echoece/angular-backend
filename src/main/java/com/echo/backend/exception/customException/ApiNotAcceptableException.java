package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiNotAcceptableException extends ApiException {

    public ApiNotAcceptableException() {
    }

    /**
     * @param message
     */
    public ApiNotAcceptableException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiNotAcceptableException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiNotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }
}
