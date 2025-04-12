package com.dodlu.core.app.utils;

import java.util.Random;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtilities {
  private static final Long MIN_NUMBER = 10L;
  private static final Long MAX_NUMBER = 1000L;
  private static final String CHARACTERS =
      "0abcde1fghij2klmno3pqrst4uvwxy5zABCD6EFGHI7JKLMN8OPQRS9TUVWXYZ0123456789";
  private static final int DEFAULT_PASSWORD_LENGTH = 30;

  @SneakyThrows
  public static String generateUsername(String email) {
    String firstPart = email.split("@")[0];
    return firstPart + randomDigit();
  }

  private static Long randomDigit() {
    return Math.round(Math.random() * (MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER);
  }

  public static String generateRandomPassword() {
    return generateRandomString(DEFAULT_PASSWORD_LENGTH);
  }

  public static String generateRandomCode(int length) {
    return generateRandomString(length);
  }

  private static String generateRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      sb.append(randomChar);
    }

    return sb.toString();
  }

  public static String generateRandomDigits(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      sb.append((int) (Math.random() * 10));
    }
    return sb.toString();
  }
}
