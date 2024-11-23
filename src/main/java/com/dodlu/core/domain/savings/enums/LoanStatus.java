package com.dodlu.core.domain.savings.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum LoanStatus {
  PENDING("Pending"),
  REJECTED("Rejected"),
  APPROVED("Approved"),
  UNKNOWN("Unknown");

  private final String name;

  LoanStatus(String name) {
    this.name = name;
  }

  @JsonCreator
  public static LoanStatus create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(LoanStatus.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(PENDING);
  }
}
