package com.dodlu.core.app.utils;

import java.util.Collection;
import java.util.stream.Stream;

public class StringUtilities {
  public static boolean isNullOrBlank(String string) {
    return string == null || string.isBlank();
  }

  public static <T> Stream<T> emptyIfNullStream(Collection<T> list) {
    return list == null ? Stream.empty() : list.stream();
  }
}
