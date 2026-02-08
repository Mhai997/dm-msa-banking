package com.pichincha.dm.msa.banking.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .orElse("Validation error");
    return build(HttpStatus.BAD_REQUEST, msg, req.getRequestURI());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req.getRequestURI());
  }

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, String path) {
    ErrorResponse body = new ErrorResponse(
        LocalDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        message,
        path
    );
    return ResponseEntity.status(status).body(body);
  }
}
