package com.cuenta_bancaria.exceptions.infra;

import com.cuenta_bancaria.exceptions.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        return buildError("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> insufficientBalance(InsufficientBalanceException ex) {
        return buildError("INSUFFICIENT_FUNDS", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExists(EntityAlreadyExistsException ex) {
        return buildError("ALREADY_EXISTS", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityInactiveException.class)
    public ResponseEntity<ErrorResponse> entityInactive(EntityInactiveException ex) {
        return buildError("ENTITY_INACTIVE", ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> invalidAmoun(InvalidAmountException ex) {
        return buildError("INVALID_AMOUNT", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> buildError(String code, String message, HttpStatus status) {
        ErrorResponse error = ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, status);
    }

}
