package com.goalglo.backend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import software.amazon.awssdk.services.ses.model.SesException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

   private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
      log.error("Unhandled exception: ", ex);
      ErrorResponse error = new ErrorResponse("An unexpected error occurred.");
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
      log.warn("Resource not found: {}", ex.getMessage());
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
      String errorMessage = ex.getBindingResult().getFieldErrors().stream()
         .map(err -> err.getField() + ": " + err.getDefaultMessage())
         .collect(Collectors.joining(", "));
      log.warn("Validation failed: {}", errorMessage);
      ErrorResponse error = new ErrorResponse(errorMessage);
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(MissingServletRequestParameterException.class)
   public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
      String errorMessage = "Missing parameter: " + ex.getParameterName();
      log.warn(errorMessage);
      ErrorResponse error = new ErrorResponse(errorMessage);
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(SesException.class)
   public ResponseEntity<ErrorResponse> handleSesException(SesException ex) {
      log.error("SES exception: {}", ex.getMessage());
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(AuthenticationException.class)
   public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
      log.warn("Unauthorized access: {}", ex.getMessage());
      ErrorResponse error = new ErrorResponse("Unauthorized access");
      return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
   }

}