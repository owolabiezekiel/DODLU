package com.dodlu.core.app.exceptions;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public abstract class ExceptionUtil<U extends ApiException> {
  private static final Logger log = LoggerFactory.getLogger(ExceptionUtil.class);

  public ExceptionUtil() {}

  public abstract U createCustomException(String var1, Throwable var2, int var3);

  public abstract Class<U> customExceptionClass();

  public <T> CompletableFuture<T> notImplementedFeature(String userTpe) {
    return this.completeExceptionally(
        this.handleCreateNotImplementedException(
            String.format("This feature is yet to be supported for the %s", userTpe)));
  }

  public <T> CompletableFuture<T> completeExceptionally(Throwable ex) {
    CompletableFuture<T> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(ex);
    return failedFuture;
  }

  public CompletableFuture<Boolean> completeBooleanExceptionally(String message) {
    return this.completeExceptionally(
        this.createCustomException(message, null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

  public <T> Function<Throwable, T> reportException(String msg) {
    return (rootEx) -> this.handleRethrowException(msg, rootEx);
  }

  public RuntimeException handleCreateException(String msg, Throwable rootEx) {
    Throwable ex = unpackCompletionException(rootEx);
    Class<U> clazz = this.customExceptionClass();
    return clazz.isAssignableFrom(ex.getClass())
        ? new CompletionException(ex)
        : this.createCustomException(msg, ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  public RuntimeException handleCreateNotImplementedException(String msg, Object... args) {
    return this.customMgtException(String.format(msg, args), HttpStatus.NOT_IMPLEMENTED.value());
  }

  public RuntimeException handleCreateException(String msg, Object... args) {
    return this.customMgtException(
        String.format(msg, args), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  public RuntimeException handleCreateException(String msg, HttpStatus code, Object... args) {
    return this.customMgtException(String.format(msg, args), code.value());
  }

  public RuntimeException handleCreateBadRequest(String msg, Object... args) {
    return this.handleCreateException(msg, HttpStatus.BAD_REQUEST, args);
  }

  public RuntimeException handleCreateNotFoundException(String msg, Object... args) {
    return this.handleCreateException(msg, HttpStatus.NOT_FOUND, args);
  }

  public RuntimeException handleCreateUnAuthorized(String msg) {
    return this.handleCreateException(msg, HttpStatus.UNAUTHORIZED);
  }

  public RuntimeException handleCreateForbidden(String msg) {
    return this.handleCreateException(msg, HttpStatus.FORBIDDEN);
  }

  public <T> T handleRethrowException(String msg, Throwable rootEx) {
    throw this.handleCreateException(msg, rootEx);
  }

  private U customMgtException(String msg, Throwable ex) {
    logException(msg, ex);
    return this.createCustomException(msg, ex, ExceptionUtil.Formatter.findExceptionCode(ex));
  }

  private U customMgtException(String msg, int code) {
    log.error(msg);
    return this.createCustomException(msg, null, code);
  }

  private static Throwable unpackCompletionException(Throwable e) {
    return e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;
  }

  private static void logException(String msg, Throwable ex) {
    Optional<ApiException> apiException = ExceptionUtil.Formatter.findApiException(ex);
    if (apiException.isPresent()) {
      ApiException apiEx = apiException.get();
      log.error(
          msg + ",\n error code: {}\nresponseHeaders: {}\nresponseBody: {}\n",
          apiEx.getCode(),
          apiEx.getResponseHeaders(),
          apiEx.getResponseBody(),
          ex);
    } else {
      log.error(msg, ex);
    }
  }

  private static class Formatter {
    private Formatter() {}

    private static int findExceptionCode(Throwable ex) {
      return findApiException(ex)
          .map(ApiException::getCode)
          .orElse(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static Optional<ApiException> findApiException(Throwable ex) {
      Stream<Throwable> var10000 = ExceptionUtils.getThrowableList(ex).stream();
      Objects.requireNonNull(ApiException.class);
      var10000 = var10000.filter(ApiException.class::isInstance);
      Objects.requireNonNull(ApiException.class);
      return var10000.map(ApiException.class::cast).findFirst();
    }
  }
}
