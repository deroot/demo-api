package com.example.demoapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // query parameter is missing.

        String errorMessage = ex.getParameterName() + "query parameter is required";

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();

        error.getErrors().add(errorMessage);

        log.error("Api Error Request: {}", error);

        return super.handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // model validation @NotNull, @NotBlank, @Size,...

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();

        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String errorMessage = String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage());
            error.getErrors().add(errorMessage);
        }

        log.error("Api Error Request: {}", error);

        return super.handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // format validation @DateFormat, Enum

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();

        String errorMessage = "Malformed JSON request";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {

            InvalidFormatException e = (InvalidFormatException) ex.getCause();

            if (e.getTargetType().getEnumConstants() != null) {
                List<String> enumList = new ArrayList<>();
                for (Object objEnum : e.getTargetType().getEnumConstants()) {
                    enumList.add(objEnum.toString());
                }
                errorMessage = String.format("JSON request is invalid value '%s' should be one of (%s)", e.getValue(), String.join(",", enumList));
            } else if (e.getTargetType().equals(Date.class)) {
                errorMessage = String.format("JSON request is invalid value '%s' could not be converted to type Date", e.getValue());
            }

        }

        error.getErrors().add(errorMessage);

        log.error("Api Error Request: {}", error);

        return super.handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, final WebRequest request) {

        // request body type miss matched.

        String errorMessage = String.format("The parameter '%s' could not be converted to type '%s'", ex.getName(), ex.getRequiredType().getSimpleName());

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build();

        error.getErrors().add(errorMessage);

        log.error("Api Error Request: {}", error);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
