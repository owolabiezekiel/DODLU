package com.dodlu.core.domain.approval.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum ApprovalStatus {
  PENDING("Pending"),
  PENDING_GROUP_ADMIN_APPROVAL("Pending group admin approval"),
  PENDING_USER_APPROVAL("Pending user approval"),
  APPROVED("Approved"),
  APPROVED_BY_USER("Approved by user"),
  APPROVED_BY_GROUP_ADMIN("Approved by group admin"),
  REJECTED("Rejected"),
  REJECTED_BY_USER("Rejected by user"),
  REJECTED_BY_GROUP_ADMIN("Rejected by group admin"),
  UNKNOWN("Unknown");

  private final String name;

  ApprovalStatus(String name) {
    this.name = name;
  }

  @JsonCreator
  public static ApprovalStatus create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(ApprovalStatus.values())
        .filter(s -> s.name().equalsIgnoreCase(value) || s.name.equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }

  @JsonValue
  public String getName() {
    return this.name;
  }
}
