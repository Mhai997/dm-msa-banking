package com.pichincha.dm.msa.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  public record ApiError(OffsetDateTime timestamp, String message) {}

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError(OffsetDateTime.now(), ex.getMessage()));
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ApiError> handleInsufficient(InsufficientBalanceException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiError(OffsetDateTime.now(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiError(OffsetDateTime.now(), "Unexpected error"));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handleBusinessException(BusinessException ex) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
  }
}
