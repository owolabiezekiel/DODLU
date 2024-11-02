package com.dodlu.core.domain.approval.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum WorkflowType {
  INDIVIDUAL("Individual"),
  ROLE("Role"),
  UNKNOWN("Unknown");

  private final String name;

  WorkflowType(String name) {
    this.name = name;
  }

  @JsonCreator
  public static WorkflowType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(WorkflowType.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }

  @JsonValue
  public String getName() {
    return this.name;
  }
}
