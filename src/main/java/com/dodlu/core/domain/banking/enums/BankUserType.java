package com.dodlu.core.domain.banking.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

public enum BankUserType {
  INDIVIDUAL("Individual"),
  GROUP("Group"),
  UNKNOWN("Unknown");

  private final String name;

  BankUserType(String name) {
    this.name = name;
  }

  @JsonCreator
  public static BankUserType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(BankUserType.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }

  @JsonValue
  public String getName() {
    return this.name;
  }
}
