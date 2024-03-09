package com.dodlu.core.app.utils;

import java.util.*;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

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

  public static Set<String> toSet(String data) {
    if (isNullOrBlank(data)) return new HashSet<>();
    return new HashSet<>(Arrays.asList(data.split(",")));
  }

  public static String toStringFromSet(Set<String> data) {
    return String.join(",", emptyIfNull(data));
  }
}
