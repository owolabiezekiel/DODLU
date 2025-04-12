package com.dodlu.core.app.utils;

import java.time.Period;
import java.util.*;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class StringUtilities {
  public static boolean isNullOrBlank(String string) {
    return string == null || string.isBlank();
  }

  public static <T> Stream<T> emptyIfNullStream(Collection<T> list) {
    return list == null ? Stream.empty() : list.stream();
  }

  public static <T> Collection<T> emptyIfNull(Collection<T> list) {
    return (list == null) ? List.of() : list;
  }

  public static String toStringFromSet(Set<String> data) {
    return String.join(",", emptyIfNull(data));
  }

  public static String formatPeriod(Period period) {
    int years = period.getYears();
    int months = period.getMonths();
    int days = period.getDays();

    if (years <= 0) {
      return String.format("%d months and %d days", months, days);
    }
    if (months <= 0) {
      return String.format("%d years and %d days", years, days);
    }

    if (days <= 0) {
      return String.format("%d years and %d months", years, months);
    }

    return String.format("%d years, %d months, and %d days", years, months, days);
  }

  public static String extractLastDigits(String input, int length) {
    if (input.length() <= length) {
      return input;
    }
    return input.substring(input.length() - length);
  }

  public static String extractFirstChars(String input, int stopLength) {
    if (input.length() <= stopLength) {
      return input;
    }
    return input.substring(0, stopLength);
  }

  public static boolean stringsMatch(String s1, String s2) {
    return s1.equalsIgnoreCase(s2);
  }

  public static String shortenString(String input, int length) {
    if (StringUtils.isNotBlank(input)) {
      if (input.length() <= length) {
        return input;
      } else {
        return input.substring(0, length - 3) + "...";
      }
    } else {
      return "";
    }
  }
}
