package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ApiBadRequestException extends ApiException {

    public ApiBadRequestException() {
    }

    /**
     * @param message
     */
    public ApiBadRequestException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiBadRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
