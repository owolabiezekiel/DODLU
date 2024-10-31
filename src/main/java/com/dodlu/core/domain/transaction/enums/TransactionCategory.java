package com.dodlu.core.domain.transaction.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum TransactionCategory {
  CREDIT("CREDIT"),
  DEBIT("DEBIT"),
  UNKNOWN("UNKNOWN");

  private final String name;

  TransactionCategory(String name) {
    this.name = name;
  }

  @JsonCreator
  public static TransactionCategory create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(TransactionCategory.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
