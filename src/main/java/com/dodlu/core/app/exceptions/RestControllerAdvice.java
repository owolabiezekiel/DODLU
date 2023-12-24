package com.dodlu.core.app.exceptions;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.dodlu.core.app.model.response.ResponseModel;
import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice<T> extends ResponseEntityExceptionHandler {
  @ExceptionHandler(value = {FileUploadException.class, NoSuchElementException.class})
  public ResponseEntity<ResponseModel<T>> handleUploadException(Exception ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(
            null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()),
        INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {CompletionException.class})
  public ResponseEntity<ResponseModel<T>> handleCompletionException(CompletionException ex) {
    if (ex.getCause() instanceof CoreException) {
      return handleDomainException((CoreException) ex.getCause());
    }
    return handleException(ex);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  public ResponseEntity<ResponseModel<T>> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(
            null, ex.getMessage(), singletonList(ex.getMessage()), FORBIDDEN.value()),
        FORBIDDEN);
  }

  @ExceptionHandler(value = {CoreException.class})
  public ResponseEntity<ResponseModel<T>> handleDomainException(CoreException ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(ex.getMessage()), ex.getCode()),
        valueOf(ex.getCode()));
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<ResponseModel<T>> handleConstraintViolation(
      ConstraintViolationException ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(
            null, "", ErrorFormatter.format(ex.getConstraintViolations()), BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {BadCredentialsException.class})
  public ResponseEntity<ResponseModel<T>> handleBadCredentialsException(
      BadCredentialsException ex) {
    log.error("Error: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(ex.getMessage()), UNAUTHORIZED.value()),
        UNAUTHORIZED);
  }

  @ExceptionHandler(value = {MultipartException.class})
  public ResponseEntity<ResponseModel<T>> handleBadRequestException(
      Exception ex, WebRequest request) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    String msg =
        String.format(
            "Exception message [%s] %n Metadata [%s]",
            ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(msg), BAD_REQUEST.value()), BAD_REQUEST);
  }

  @ExceptionHandler(value = {ApiException.class})
  public ResponseEntity<ResponseModel<T>> handleApiException(ApiException ex) {
    String msg = String.format("Exception message [%s]", ex.getMessage());
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(msg), BAD_REQUEST.value()), BAD_REQUEST);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<ResponseModel<T>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(ex.getMessage()), BAD_REQUEST.value()),
        BAD_REQUEST);
  }

  @ExceptionHandler(value = {Throwable.class})
  public ResponseEntity<ResponseModel<T>> handleException(Throwable ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(
            null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()),
        INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ResponseModel<T>> handleConverterErrors(
      MethodArgumentTypeMismatchException ex) {
    if (ex.getCause() instanceof IllegalArgumentException) {
      return handleIllegalArgumentException((IllegalArgumentException) ex.getCause());
    }
    return handleException(ex);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    BindingResult bindingResult = ex.getBindingResult();
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", ErrorFormatter.format(bindingResult), BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    return new ResponseEntity<>(
        new ResponseModel<>(null, "", singletonList(ex.getMessage()), BAD_REQUEST.value()),
        HttpStatus.BAD_REQUEST);
  }
}
