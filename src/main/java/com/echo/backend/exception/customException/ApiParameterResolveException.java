package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiParameterResolveException extends ApiException {

    private static final long serialVersionUID = 7729045683866048832L;

    public ApiParameterResolveException() {
    }

    ApiParameterResolveException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getServiceStatus() {
        return null;
    }
}
