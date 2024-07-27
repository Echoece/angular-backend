package com.echo.backend.exception;

import com.echo.backend.exception.dto.ErrorDTO;
import com.echo.backend.exception.dto.ErrorDetailDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.echo.backend.exception.ExceptionConstants.*;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

// Extending ResponseEntityExceptionHandler is beneficial if you want to leverage Spring's default exception
// handling and customize specific cases. However, if your application's needs are simple or if you have a custom
// exception handling strategy, you might choose not to extend it.

/*
   Advantages of Extending ResponseEntityExceptionHandler:
        1. Default Handling: It provides default handling for standard exceptions like MethodArgumentNotValidException,
            HttpMessageNotReadableException, and others. This means you don't need to implement basic exception handling
            from scratch.

        2. Customization: You can override methods to customize the response for specific exceptions. For example, you
            can override handleMethodArgumentNotValid to customize the response for validation errors.

        3. Centralized Handling: It allows you to centralize your exception handling logic in one place, making it
            easier to manage and maintain.

    When You Might Not Need to Extend ResponseEntityExceptionHandler:
        1. Simple Applications: If your application has simple exception handling needs and does not require the default
            behaviors provided by ResponseEntityExceptionHandler, you might manage exceptions directly using @ExceptionHandler methods.

        2. Custom Handling Logic: If you have a completely custom approach to handling exceptions that doesn't benefit from
            the default behaviors, you might choose to implement your handling logic without extending ResponseEntityExceptionHandler.

        3. Minimal Dependency: If you want to minimize dependencies on Spring's internal classes or prefer a more explicit
            control over all aspects of exception handling, you might opt to handle exceptions without using ResponseEntityExceptionHandler.
* */

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    @Value("${app.env}")
    private String appEnv;

    @ExceptionHandler({ApiException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleExceptions(final ApiException ex, final WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        ErrorDTO errorMessageDTO = new ErrorDTO();
        errorMessageDTO.setName(ex.getServiceStatus().getReasonPhrase());
        errorMessageDTO.setStatusCode(ex.getServiceStatus().value());
        if(!appEnv.equals(APP_ENV_PROD))
            errorMessageDTO.setStackTrace(errorStackTraceToString(ex));

        ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
        String message;
        try {
            message = messageSource.getMessage(ex.getMessage(), null, locale);
        } catch (Exception e) {
            message = ex.getMessage();
        }
        errorDetailDTO.setMessage(message);
        errorMessageDTO.setDetails(Arrays.asList(errorDetailDTO));

        body.put(ERROR, errorMessageDTO);
        return handleExceptionInternal(ex, body, new HttpHeaders(), ex.getServiceStatus(), request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintExceptions(ConstraintViolationException ex, final WebRequest request) {

        String tableName  = ex.getCause().getMessage().split("\\.`")[1].split("`,")[0];
        String message;
        try {
            message = messageSource.getMessage("entity.already.used.global", new Object[]{StringUtils.capitalize(tableName.replace("_", " ")).toString()}, locale);
        } catch (Exception e) {
            message = ex.getMessage();
        }
        logger.info(message);
        ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
        errorDetailDTO.setMessage(message);
        Map<String, Object> body = new LinkedHashMap<>();

        ErrorDTO errorMessageDTO = new ErrorDTO();
        errorMessageDTO.setName(HttpStatus.CONFLICT.getReasonPhrase());
        errorMessageDTO.setStatusCode(HttpStatus.CONFLICT.value());
        if(!appEnv.equals(APP_ENV_PROD))
            errorMessageDTO.setStackTrace(errorStackTraceToString(ex));
        errorMessageDTO.setDetails(Arrays.asList(errorDetailDTO));

        body.put(ERROR, errorMessageDTO);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // logger.info(ex.getMessage());
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

        List<ErrorDetailDTO> errorDetailDTOList = fieldErrors.stream().map(fieldError -> {
            ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
            errorDetailDTO.setFieldName(fieldError.getField());
            errorDetailDTO.setValue(isNull(fieldError.getRejectedValue()) ? null : fieldError.getRejectedValue().toString());
            errorDetailDTO.setMessage(fieldError.getField() + " " + fieldError.getDefaultMessage());
            return errorDetailDTO;
        }).collect(toList());

        if(!globalErrors.isEmpty()){
            errorDetailDTOList.addAll(globalErrors.stream().map(globalError -> {
                ErrorDetailDTO errorDetailDTO = new ErrorDetailDTO();
                errorDetailDTO.setFieldName(globalError.getObjectName());
                String message;
                if(globalError.getCode().contains("."))
                    message = messageSource.getMessage(globalError.getCode(), globalError.getArguments(), locale);
                else
                    message = globalError.getCode();
                errorDetailDTO.setMessage(globalError.getObjectName() + ", " + (message.isEmpty() ? globalError.getDefaultMessage() : message));
                return errorDetailDTO;
            }).toList());
        }

        Map<String, Object> body = new LinkedHashMap<>();

        ErrorDTO errorMessageDTO = new ErrorDTO();
        errorMessageDTO.setName(resolveReasonFromStatusCode(status));
        errorMessageDTO.setStatusCode(status.value());
        if(!appEnv.equals(APP_ENV_PROD) )
            errorMessageDTO.setStackTrace(errorStackTraceToString(ex));
        errorMessageDTO.setDetails(errorDetailDTOList);


        body.put(ERROR, errorMessageDTO);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS, status.value());
        response.put(MESSAGE, messageSource.getMessage("required.query.param", new Object[]{ex.getParameterName()}, locale));
        body.put(ERROR, response);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS, status.value());
        response.put(MESSAGE, messageSource.getMessage("required.path.param", new Object[]{ex.getVariableName()}, locale));
        body.put(ERROR, response);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        //Map<String, Object> body = buildErrorFormat(ex, status);
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS, status.value());
        response.put(MESSAGE, ex.getMessage());
        body.put(ERROR, response);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS, status.value());
        response.put(MESSAGE, ex.getMessage());
        body.put(ERROR, response);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(STATUS, status.value());
        response.put(MESSAGE, ex.getMessage());
        body.put(ERROR, response);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    private String errorStackTraceToString(Exception ex) {
        try {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String resolveReasonFromStatusCode(HttpStatusCode status){
        return HttpStatus.valueOf(status.value()).getReasonPhrase();
    }

}
