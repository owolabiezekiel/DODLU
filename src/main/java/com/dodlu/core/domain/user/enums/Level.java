package com.dodlu.core.domain.user.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Level {
  LEVEL_200,
  LEVEL_300,
  LEVEL_400;

  @JsonCreator
  public static Level create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Level.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(LEVEL_200);
  }
}
