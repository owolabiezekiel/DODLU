package com.dodlu.core.domain.user.model;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum UserType {
  ADMIN,
  EMPLOYER,
  COORDINATOR,
  SUPERVISOR,
  STUDENT,
  LECTURER,
  UNKNOWN;

  @JsonCreator
  public static UserType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(UserType.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
