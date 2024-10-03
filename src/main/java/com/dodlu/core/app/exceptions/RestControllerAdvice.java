package com.dodlu.core.app.exceptions;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.dodlu.core.app.model.response.ResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class RestControllerAdvice<T> {
  private final ObjectMapper objectMapper;

  public RestControllerAdvice(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(value = {FileUploadException.class, NoSuchElementException.class})
  public ResponseEntity<ResponseModel<T>> handleUploadException(Exception ex) {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(
        new ResponseModel<>(
            null, "", singletonList(ex.getMessage()), INTERNAL_SERVER_ERROR.value()),
        INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {CompletionException.class})
  public ResponseEntity<ResponseModel<T>> handleCompletionException(
      CompletionException ex, HttpServletResponse response) throws IOException {
    if (ex.getCause() instanceof CoreException) {
      return handleDomainException((CoreException) ex.getCause(), response);
    }
    return handleException(ex);
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  public void handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse response)
      throws IOException {
    log.error("Unhandled exception encountered: {}", ex.getMessage(), ex);
    ResponseModel<?> responseModel =
        new ResponseModel<>(
            null, ex.getMessage(), Collections.singletonList(ex.getMessage()), FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(FORBIDDEN.value());
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
  }

  @ExceptionHandler(value = {CoreException.class})
  public ResponseEntity<ResponseModel<T>> handleDomainException(
      CoreException ex, HttpServletResponse response) throws IOException {
    log.error("CoreException encountered: {}", ex.getMessage(), ex);

    ResponseModel<?> responseModel =
        new ResponseModel<>(
            null, ex.getMessage(), Collections.singletonList(ex.getMessage()), ex.getCode());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(ex.getCode());
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
    return null;
  }

  @ExceptionHandler(value = {NoResourceFoundException.class})
  public ResponseEntity<ResponseModel<T>> handleNoResourceFound(
      NoResourceFoundException ex, HttpServletResponse response) throws IOException {
    log.error("No resource found exception encountered: {}", ex.getMessage());

    ResponseModel<?> responseModel =
        new ResponseModel<>(
            null, ex.getMessage(), Collections.singletonList(ex.getMessage()), BAD_REQUEST.value());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(BAD_REQUEST.value());
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
    return null;
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

  @ExceptionHandler(CustomBadCredentialsException.class)
  public void handleCustomUnauthorizedException(CustomBadCredentialsException ex)
      throws IOException {
    HttpServletResponse response = ex.getResponse();
    String message = ex.getMessage();
    HttpStatus status = HttpStatus.UNAUTHORIZED;

    ResponseModel<Object> responseModel =
        new ResponseModel<>(null, message, Collections.singletonList(message), status.value());

    response.setHeader("Content-Type", "application/json");
    response.setStatus(status.value());
    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
  }

  @ExceptionHandler(value = {ExpiredJwtException.class})
  public ResponseEntity<ResponseModel<T>> handleTokenExpiredException(ExpiredJwtException ex) {
    log.error("Token has expired {}", ex.getMessage(), ex);
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public void handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
    BindingResult bindingResult = ex.getBindingResult();
    var formattedBinding = ErrorFormatter.format(bindingResult);
    ResponseModel<?> responseModel =
        new ResponseModel<>(null, formattedBinding.get(0), formattedBinding, 400);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(BAD_REQUEST.value());
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
  }

  @ExceptionHandler(Exception.class)
  public void handleGenericException(Exception ex, HttpServletResponse response)
      throws IOException {
    log.error("Unexpected exception encountered: {}", ex.getMessage(), ex);

    ResponseModel<?> responseModel =
        new ResponseModel<>(
            null,
            "",
            Collections.singletonList("An unexpected error occurred"),
            HttpStatus.INTERNAL_SERVER_ERROR.value());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.getWriter().write(objectMapper.writeValueAsString(responseModel));
  }
}
