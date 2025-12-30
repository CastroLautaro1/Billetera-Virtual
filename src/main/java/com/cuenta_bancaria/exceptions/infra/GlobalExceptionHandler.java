package com.cuenta_bancaria.exceptions.infra;

import com.cuenta_bancaria.exceptions.domain.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        return buildError("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> insufficientBalance(InsufficientBalanceException ex) {
        return buildError("INSUFFICIENT_FUNDS", ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExists(EntityAlreadyExistsException ex) {
        return buildError("ALREADY_EXISTS", ex.getMessage(), HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(EntityInactiveException.class)
    public ResponseEntity<ErrorResponse> entityInactive(EntityInactiveException ex) {
        return buildError("ENTITY_INACTIVE", ex.getMessage(), HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorResponse> invalidAmoun(InvalidAmountException ex) {
        return buildError("INVALID_AMOUNT", ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    // Captura errores de los Records
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return buildError("VALIDATION_ERROR", ex.getMessage(), HttpStatus.BAD_REQUEST, errors);
    }

    // Captura errores de persistencia
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String message = "El recurso ya existe o viola una restricción de integridad.";
        return buildError("INTEGRITY_ERROR", message, HttpStatus.CONFLICT, null);
    }

    // Metodo auxiliar para construir los ErrorResponse
    public ResponseEntity<ErrorResponse> buildError(String code, String message, HttpStatus status, Map<String, String> details) {
        ErrorResponse error = ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
        return new ResponseEntity<>(error, status);
    }

}
