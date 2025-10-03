package com.echo.backend.exception;

import com.echo.backend.exception.dto.ErrorDTO;
import com.echo.backend.exception.dto.ErrorDetailDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
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
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.*;

import static com.echo.backend.exception.ExceptionConstants.*;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerV2 extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerV2.class);

    private final MessageSource messageSource;

    @Value("${app.env}")
    private String appEnv;

    // ---------------- Helper Methods ----------------

    private Map<String, Object> buildErrorBody(HttpStatusCode status, List<ErrorDetailDTO> details, Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setName(resolveReasonFromStatusCode(status));
        errorDTO.setStatusCode(status.value());
        errorDTO.setTimestamp(Instant.now().toString());
        if (!APP_ENV_PROD.equals(appEnv)) {
            errorDTO.setStackTrace(errorStackTraceToString(ex));
        }
        if (details != null && !details.isEmpty()) {
            errorDTO.setMessage(details.get(0).getMessage()); // general summary
        }
        errorDTO.setDetails(details);

        // traceId from MDC or random UUID
        String traceId = Optional.ofNullable(MDC.get("traceId")).orElse(UUID.randomUUID().toString());
        errorDTO.setTraceId(traceId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ERROR, errorDTO);
        return body;
    }

    private String errorStackTraceToString(Exception ex) {
        try (StringWriter errors = new StringWriter(); PrintWriter pw = new PrintWriter(errors)) {
            ex.printStackTrace(pw);
            return errors.toString();
        } catch (Exception e) {
            logger.error("Failed to extract stack trace", e);
        }
        return "";
    }

    private String resolveReasonFromStatusCode(HttpStatusCode status) {
        return HttpStatus.valueOf(status.value()).getReasonPhrase();
    }

    private String getLocalizedMessage(String code, Object[] args) {
        try {
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return code;
        }
    }

    // ---------------- Custom Exceptions ----------------

    @ExceptionHandler({ApiException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleCustomExceptions(Exception ex, WebRequest request) {
        List<ErrorDetailDTO> details = new ArrayList<>();
        ErrorDetailDTO detail = new ErrorDetailDTO();
        if (ex instanceof ApiException apiEx) {
            detail.setMessage(getLocalizedMessage(apiEx.getMessage(), null));
            details.add(detail);
            return handleExceptionInternal(apiEx, buildErrorBody(apiEx.getServiceStatus(), details, apiEx),
                    new HttpHeaders(), apiEx.getServiceStatus(), request);
        } else {
            detail.setMessage(getLocalizedMessage(ex.getMessage(), null));
            details.add(detail);
            return handleExceptionInternal(ex, buildErrorBody(HttpStatus.FORBIDDEN, details, ex),
                    new HttpHeaders(), HttpStatus.FORBIDDEN, request);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintExceptions(ConstraintViolationException ex, WebRequest request) {
        String message;
        try {
            String tableName = Optional.ofNullable(ex.getConstraintName()).orElse("entity");
            message = getLocalizedMessage("entity.already.used.global",
                    new Object[]{StringUtils.capitalize(tableName.replace("_", " "))});
        } catch (Exception e) {
            message = ex.getMessage();
        }

        logger.info(message);

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(message);
        return handleExceptionInternal(ex, buildErrorBody(HttpStatus.CONFLICT, Collections.singletonList(detail), ex),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // ---------------- Spring MVC Overrides ----------------

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        List<ErrorDetailDTO> details = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ErrorDetailDTO detail = new ErrorDetailDTO();
            detail.setFieldName(fieldError.getField());
            detail.setValue(Objects.toString(fieldError.getRejectedValue(), null));
            detail.setMessage(fieldError.getField() + " " + fieldError.getDefaultMessage());
            details.add(detail);
        });

        ex.getBindingResult().getGlobalErrors().forEach(globalError -> {
            ErrorDetailDTO detail = new ErrorDetailDTO();
            detail.setFieldName(globalError.getObjectName());
            String message = globalError.getCode().contains(".")
                    ? getLocalizedMessage(globalError.getCode(), globalError.getArguments())
                    : globalError.getCode();
            detail.setMessage(globalError.getObjectName() + ", " + (message.isEmpty() ? globalError.getDefaultMessage() : message));
            details.add(detail);
        });

        return handleExceptionInternal(ex, buildErrorBody(status, details, ex), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(getLocalizedMessage("required.query.param", new Object[]{ex.getParameterName()}));

        return handleExceptionInternal(ex, buildErrorBody(status, Collections.singletonList(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(getLocalizedMessage("required.path.param", new Object[]{ex.getVariableName()}));

        return handleExceptionInternal(ex, buildErrorBody(status, Collections.singletonList(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, buildErrorBody(status, Collections.singletonList(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, buildErrorBody(status, Collections.singletonList(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(ex.getMessage());

        return handleExceptionInternal(ex, buildErrorBody(status, Collections.singletonList(detail), ex),
                headers, status, request);
    }

    // ---------------- Additional Spring Exceptions ----------------

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        String msg = "Method " + ex.getMethod() + " not allowed. Supported: " + ex.getSupportedHttpMethods();
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(msg);

        return handleExceptionInternal(ex, buildErrorBody(status, List.of(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        String msg = "Content type " + ex.getContentType() + " not supported. Supported: " + ex.getSupportedMediaTypes();
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(msg);

        return handleExceptionInternal(ex, buildErrorBody(status, List.of(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage("Requested media type not acceptable");

        return handleExceptionInternal(ex, buildErrorBody(status, List.of(detail), ex),
                headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        String msg = "Parameter '" + ex.getPropertyName() + "' should be of type " + ex.getRequiredType();
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(msg);

        return handleExceptionInternal(ex, buildErrorBody(status, List.of(detail), ex),
                headers, status, request);
    }

    // ---------------- Generic Fallback ----------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error("Unhandled exception: ", ex);
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, List.of(detail), ex),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleIllegalArguments(RuntimeException ex, WebRequest request) {
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage(ex.getMessage());
        return handleExceptionInternal(ex, buildErrorBody(HttpStatus.BAD_REQUEST, List.of(detail), ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // ---------------- Optional: DB / File Exceptions ----------------

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        logger.error("Database error: ", ex);
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage("Database error occurred");
        return handleExceptionInternal(ex, buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, List.of(detail), ex),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSize(MaxUploadSizeExceededException ex, WebRequest request) {
        ErrorDetailDTO detail = new ErrorDetailDTO();
        detail.setMessage("Uploaded file is too large. Maximum allowed size exceeded.");
        return handleExceptionInternal(ex, buildErrorBody(HttpStatus.PAYLOAD_TOO_LARGE, List.of(detail), ex),
                new HttpHeaders(), HttpStatus.PAYLOAD_TOO_LARGE, request);
    }
}
