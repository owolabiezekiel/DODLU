package com.dodlu.core.domain.savings.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Currency {
  NGN("NGN");

  private final String name;

  Currency(String name) {
    this.name = name;
  }

  @JsonCreator
  public static Currency create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(Currency.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(NGN);
  }
}
