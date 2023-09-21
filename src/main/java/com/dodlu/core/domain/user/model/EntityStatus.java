package com.dodlu.core.domain.user.model;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum EntityStatus {
  ACTIVE,
  INACTIVE,
  UNKNOWN;

  @JsonCreator
  public static EntityStatus create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(EntityStatus.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
