package com.dodlu.core.app.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
public class CustomBadCredentialsException extends RuntimeException {
  private final HttpServletResponse response;

  public CustomBadCredentialsException(String message, HttpServletResponse response) {
    super(message);
    this.response = response;
  }
}
