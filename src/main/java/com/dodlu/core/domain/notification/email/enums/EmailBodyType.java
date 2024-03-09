package com.dodlu.core.domain.notification.email.enums;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum EmailBodyType {
  HTML("text/html"),
  TEXT("text/plain");

  private final String contentType;

  EmailBodyType(String contentType) {
    this.contentType = contentType;
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
