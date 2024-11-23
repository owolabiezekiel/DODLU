package com.dodlu.core.domain.savings.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum RepaymentStatus {
  PENDING("Pending"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed"),
  UNKNOWN("Unknown");

  private final String name;

  RepaymentStatus(String name) {
    this.name = name;
  }

  @JsonCreator
  public static RepaymentStatus create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(RepaymentStatus.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(PENDING);
  }
}
