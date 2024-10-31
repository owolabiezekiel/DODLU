package com.dodlu.core.domain.transaction.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum TransactionType {
  GROUP_TO_CUSTOMER("GROUP TO CUSTOMER"),
  CUSTOMER_TO_CUSTOMER("CUSTOMER TO CUSTOMER"),
  CUSTOMER_TO_GROUP("CUSTOMER_TO_GROUP"),
  UNKNOWN("UNKNOWN");

  private final String name;

  TransactionType(String name) {
    this.name = name;
  }

  @JsonCreator
  public static TransactionType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(TransactionType.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
