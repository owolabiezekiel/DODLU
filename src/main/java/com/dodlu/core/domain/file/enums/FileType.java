package com.dodlu.core.domain.file.enums;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum FileType {
  RESUME,
  COVER_LETTER,
  PROFILE_PICTURE,
  UNKNOWN;

  @JsonCreator
  public static FileType create(String value) {
    if (isNullOrBlank(value)) {
      return null;
    }

    return Stream.of(FileType.values())
        .filter(e -> e.toString().equalsIgnoreCase(value))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
