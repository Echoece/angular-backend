package com.echo.backend.exception.customException;

import com.echo.backend.exception.ApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

import java.util.Locale;



public class ApiNotCreatedException extends ApiException {

    private static Locale locale = LocaleContextHolder.getLocale();
    private boolean needToAlert = true;
    private  static String message;

    public ApiNotCreatedException() {
        super();
    }

    /**
     * @param message
     */
    public ApiNotCreatedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiNotCreatedException(Throwable cause) {
        super(cause.getClass().getName() + message, cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiNotCreatedException(String message, Throwable cause) {
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

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @PostConstruct
    @Autowired
    public void init(MessageSource messageSource){
        message = messageSource.getMessage("entity.not.created", null, locale);
    }
}
