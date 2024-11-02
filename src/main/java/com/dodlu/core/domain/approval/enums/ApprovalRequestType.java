package com.dodlu.core.domain.approval.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum ApprovalRequestType {
  LOAN("Loan"),
  GROUP_INVITE("Group Invite"),
  UNKNOWN("Unknown");

  private final String name;

  ApprovalRequestType(String name) {
    this.name = name;
  }

  @JsonCreator
  public static ApprovalRequestType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(ApprovalRequestType.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }

  @JsonValue
  public String getName() {
    return this.name;
  }
}
