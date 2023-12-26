package com.dodlu.core.domain.user.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Gender {
  MALE,
  FEMALE,
  OTHERS,
  UNKNOWN;

  @JsonCreator
  public static Gender create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Gender.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
