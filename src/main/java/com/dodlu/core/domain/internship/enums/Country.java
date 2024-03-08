package com.dodlu.core.domain.internship.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Country {
  NIGERIA;

  @JsonCreator
  public static Country create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Country.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(NIGERIA);
  }
}
