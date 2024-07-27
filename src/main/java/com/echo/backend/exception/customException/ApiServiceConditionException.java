package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import org.springframework.http.HttpStatus;


public class ApiServiceConditionException extends ApiException {

    private boolean needToAlert = false;

    public ApiServiceConditionException() {
    }

    /**
     * @param message
     */
    public ApiServiceConditionException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiServiceConditionException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiServiceConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isNeedToAlert() {
        return needToAlert;
    }

    public void setNeedToAlert(boolean needToAlert) {
        this.needToAlert = needToAlert;
    }


    public HttpStatus getServiceStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }


}
