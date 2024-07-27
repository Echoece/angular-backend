package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiUnProcessableEntityException extends ApiException {

    public ApiUnProcessableEntityException() {
    }


    /**
     * @param message
     */
    public ApiUnProcessableEntityException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiUnProcessableEntityException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiUnProcessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
