package com.dodlu.core.domain.immunization.model.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum AgeTarget {
  BIRTH("birth"),
  WEEK_SIX("Week 6"),
  WEEK_TEN("Week 10"),
  WEEK_FOURTEEN("Week 14"),

  MONTH_SIX("Month 6"),
  MONTH_NINE("Month 9"),
  MONTH_TWELVE("Month 12"),
  MONTH_FIFTEEN("Month 15"),
  YEAR_NINE("Year 9"),

  UNKNOWN("unknown");

  private final String name;

  AgeTarget(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return name;
  }

  @JsonCreator
  public static AgeTarget create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(AgeTarget.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
