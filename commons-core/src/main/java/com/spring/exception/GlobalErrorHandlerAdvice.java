package com.spring.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e) {
        var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultErrorMessage> handleBadRequestException(BadRequestException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityException(Exception e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<DefaultErrorMessage> handleEmailAlreadyExistException(EmailAlreadyExistException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                          HttpServletRequest request) {
        var defaultMessage = e.getBindingResult().getAllErrors().
                stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.joining(", "));

        ApiError apiError = ApiError.builder().timestamp(OffsetDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getRequestURI())
                .message(defaultMessage)
                .build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
