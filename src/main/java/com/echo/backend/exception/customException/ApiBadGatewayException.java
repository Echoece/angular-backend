package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiBadGatewayException extends ApiException {

    public ApiBadGatewayException() {

    }

    /**
     * @param message
     */
    public ApiBadGatewayException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiBadGatewayException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiBadGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
