package com.dodlu.core.domain.user.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Programme {
  UNDERGRADUATE,
  POSTGRADUATE;

  @JsonCreator
  public static Programme create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Programme.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNDERGRADUATE);
  }
}
