package com.faisal.smarttaskmanager.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(DomainException exception) {
        log.error("domain error", exception);
        Set<Violation> violations = exception.getViolations();
        return ResponseEntity.badRequest().body(violations);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException exception) {
        log.error("not found error", exception);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Set<Violation>> handleInvalidFormat(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException", exception);
        String message = "Invalid input format. Possibly wrong datetime format.";
        return ResponseEntity
                .badRequest()
                .body(Set.of(Violation.of("error", message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleServerError(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.internalServerError().build();
    }


}