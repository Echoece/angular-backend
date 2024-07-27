package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiNotSupportException extends ApiException {

    public ApiNotSupportException() {
    }

    /**
     * @param message
     */
    public ApiNotSupportException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiNotSupportException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    }
}
