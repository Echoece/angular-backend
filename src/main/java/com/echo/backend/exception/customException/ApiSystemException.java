package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiSystemException extends ApiException {

    private boolean needToAlert = true;

    public ApiSystemException() {
    }

    /**
     * @param message
     */
    public ApiSystemException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiSystemException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isNeedToAlert() {
        return needToAlert;
    }

    public void setNeedToAlert(boolean needToAlert) {
        this.needToAlert = needToAlert;
    }

    public HttpStatus getServiceStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
