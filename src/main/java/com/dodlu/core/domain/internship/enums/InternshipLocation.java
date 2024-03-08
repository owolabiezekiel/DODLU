package com.dodlu.core.domain.internship.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum InternshipLocation {
  REMOTE,
  HYBRID,
  ONSITE;

  @JsonCreator
  public static InternshipLocation create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(InternshipLocation.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(REMOTE);
  }
}
