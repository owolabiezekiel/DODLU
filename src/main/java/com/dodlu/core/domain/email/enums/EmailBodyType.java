package com.dodlu.core.domain.email.enums;

import java.util.stream.Stream;

public enum EmailBodyType {
  HTML("text/html"),
  TEXT("text/plain");

  private final String contentType;

  EmailBodyType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentType() {
    return this.contentType;
  }

  public static EmailBodyType create(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    return Stream.of(EmailBodyType.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(HTML);
  }
}
