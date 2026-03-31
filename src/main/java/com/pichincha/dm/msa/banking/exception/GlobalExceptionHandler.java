package com.pichincha.dm.msa.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  public record ApiError(
          OffsetDateTime timestamp,
          int status,
          String error,
          String message,
          List<String> details
  ) {}

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
    return buildResponse(
            HttpStatus.NOT_FOUND,
            "Recurso no encontrado",
            ex.getMessage(),
            null
    );
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ApiError> handleInsufficientBalance(InsufficientBalanceException ex) {
    return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Saldo insuficiente",
            ex.getMessage(),
            null
    );
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
    return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Regla de negocio inválida",
            ex.getMessage(),
            null
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

    return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Error de validación",
            "Uno o más campos tienen valores inválidos",
            details
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception ex) {
    return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Error interno del servidor",
            "Ocurrió un error inesperado",
            null
    );
  }

  private ResponseEntity<ApiError> buildResponse(HttpStatus status,
                                                 String error,
                                                 String message,
                                                 List<String> details) {
    ApiError apiError = new ApiError(
            OffsetDateTime.now(),
            status.value(),
            error,
            message,
            details
    );

    return ResponseEntity.status(status).body(apiError);
  }
}