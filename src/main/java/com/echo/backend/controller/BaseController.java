package com.echo.backend.controller;

import com.echo.backend.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public abstract class BaseController {
    protected String message;
    private Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private MessageSource messageSource;


    private String getMessage(String message) {
        try {
            return message.contains(".") ? messageSource.getMessage(message, null, locale) : message;
        } catch (NoSuchMessageException ex) {
            return message;
        }
    }

    private String getMessage(String message, Object [] objects) {
        try {
            return message.contains(".") ? messageSource.getMessage(message, objects, locale) : message;
        } catch (NoSuchMessageException ex) {
            return message;
        }
    }

    /*---- TODO: add i8n in response -----*/

    public <T> ResponseEntity<ApiResponse<Page<T>>> buildPaginatedResponse(Page<T> data) {
        return ResponseEntity.ok(ApiResponse.paginatedResponse(data));
    }

    public <T> ResponseEntity<ApiResponse<T>> buildResponse(T data){
        return new ResponseEntity<>(ApiResponse.success(data), HttpStatus.OK);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildResponse(T data, String message){
        return new ResponseEntity<>(ApiResponse.success(message, data), HttpStatus.OK);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildResponseCreated(T data){
        return new ResponseEntity<>(ApiResponse.success("Created",data, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildResponseUpdated(T data){
        return new ResponseEntity<>(ApiResponse.success("Updated",data), HttpStatus.OK);
    }

    public <T> ResponseEntity<ApiResponse<T>> buildResponseDeleted(){
        return new ResponseEntity<>(ApiResponse.success("Deleted",null), HttpStatus.NO_CONTENT);
    }



}
