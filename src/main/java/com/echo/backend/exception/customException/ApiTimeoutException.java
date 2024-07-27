package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiTimeoutException extends ApiException {

    public ApiTimeoutException() {
    }

    /**
     * @param message
     */
    public ApiTimeoutException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiTimeoutException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.REQUEST_TIMEOUT;
    }
}
