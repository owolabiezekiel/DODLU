package com.dodlu.core.app.exceptions;

import java.util.Map;

public class CoreException extends ApiException {
  public CoreException() {}

  public CoreException(String message) {
    super(message);
  }

  public CoreException(int code, String message, Throwable throwable) {
    super(message, throwable, code, Map.of());
  }
}
