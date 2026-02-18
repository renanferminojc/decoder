package com.ead.authuser.configs;

import com.ead.authuser.errors.AlreadyInUseException;
import com.ead.authuser.errors.NotFoundException;
import com.ead.authuser.errors.PasswordMismatchException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerConfig {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

    ErrorMessage error =
        new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex) {
    ErrorMessage error =
        new ErrorMessage(
            HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), List.of(ex.getMessage()));

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<ErrorMessage> passwordMismatch(PasswordMismatchException ex) {
    ErrorMessage error =
        new ErrorMessage(
            HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()));

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AlreadyInUseException.class)
  public ResponseEntity<ErrorMessage> alreadyInUse(AlreadyInUseException ex) {
    ErrorMessage error =
        new ErrorMessage(
            HttpStatus.CONFLICT.value(), LocalDateTime.now(), List.of(ex.getMessage()));

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  public record ErrorMessage(int statusCode, LocalDateTime timestamp, List<String> message) {}
}
