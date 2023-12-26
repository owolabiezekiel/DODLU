package com.dodlu.core.domain.user.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Industry {
  Telecommunication,
  Housing,
  Engineering,
  Finance,
  Banking,
  Education,
  Tourism,
  Hospitality;

  @JsonCreator
  public static Industry create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Industry.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(Education);
  }
}
