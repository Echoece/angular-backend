package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiMethodDeniedException extends ApiException {

    public ApiMethodDeniedException() {
    }

    /**
     * @param message
     */
    public ApiMethodDeniedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiMethodDeniedException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiMethodDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.METHOD_NOT_ALLOWED;
    }
}
