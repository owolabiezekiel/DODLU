package com.dodlu.core.domain.transaction.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum EntityType {
  INDIVIDUAL("INDIVIDUAL"),
  GROUP("GROUP"),
  UNKNOWN("UNKNOWN");

  private final String name;

  EntityType(String name) {
    this.name = name;
  }

  @JsonCreator
  public static EntityType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(EntityType.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
