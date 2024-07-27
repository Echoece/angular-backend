package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiAuthorizationException extends ApiException {

    public ApiAuthorizationException() {
    }


    /**
     * @param message
     */
    public ApiAuthorizationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiAuthorizationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
