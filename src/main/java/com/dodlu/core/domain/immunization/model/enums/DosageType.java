package com.dodlu.core.domain.immunization.model.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum DosageType {
  ML("ml"),
  DROP("drop(s)"),
  IU("IU"),
  UNKNOWN("unknown");

  private final String name;

  DosageType(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }

  @JsonCreator
  public static DosageType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(DosageType.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
