package com.dodlu.core.domain.savings.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum SavingFrequency {
  DAILY("Daily"),
  WEEKLY("Weekly"),
  BI_WEEKLY("Bi weekly"),
  MONTHLY("Monthly"),
  QUARTERLY("Quarterly"),
  YEARLY("Yearly");

  private final String name;

  SavingFrequency(String name) {
    this.name = name;
  }

  @JsonCreator
  public static SavingFrequency create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(SavingFrequency.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(WEEKLY);
  }
}
