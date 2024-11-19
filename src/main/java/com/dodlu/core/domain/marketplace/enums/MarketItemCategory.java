package com.dodlu.core.domain.marketplace.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum MarketItemCategory {
  FOODSTUFFS("Foodstuffs"),
  LIFESTYLE("LifeStyle"),
  ELECTRONICS("Electronics"),
  VEHICLE("Vehicle"),
  UNKNOWN("Unknown");

  private final String name;

  MarketItemCategory(String name) {
    this.name = name;
  }

  @JsonCreator
  public static MarketItemCategory create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(MarketItemCategory.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
