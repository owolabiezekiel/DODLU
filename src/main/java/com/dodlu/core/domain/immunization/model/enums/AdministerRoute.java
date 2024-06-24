package com.dodlu.core.domain.immunization.model.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum AdministerRoute {
  INTRA_DERMAL("Intra dermal"),
  ORAL("Oral"),
  INTRAMUSCULAR("Intramuscular"),
  SUBCUTANEOUS("Subcutaneous"),
  UNKNOWN("unknown");

  private final String name;

  AdministerRoute(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }

  @JsonCreator
  public static AdministerRoute create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(AdministerRoute.values())
        .filter(e -> e.name().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
