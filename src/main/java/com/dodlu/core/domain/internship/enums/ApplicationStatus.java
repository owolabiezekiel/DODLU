package com.dodlu.core.domain.internship.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum ApplicationStatus {
  PENDING,
  REVIEW,
  ACCEPTED_BY_EMPLOYER,
  ACCEPTED_BY_STUDENT,
  REJECTED_BY_EMPLOYER,
  REJECTED_BY_STUDENT;

  @JsonCreator
  public static ApplicationStatus create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(ApplicationStatus.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(PENDING);
  }
}
