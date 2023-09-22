package com.dodlu.core.app.utils;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtilities {
  private static final Long MIN_NUMBER = 40L;
  private static final Long MAX_NUMBER = 100L;

  @SneakyThrows
  public static String generateUsername(String email) {
    var messageDigest = MessageDigest.getInstance("MD5");

    messageDigest.update(email.getBytes());
    byte[] digest = messageDigest.digest();

    String emailHash = DatatypeConverter.printHexBinary(digest).toLowerCase();

    return emailHash.substring(0, 3).strip() + randomDigit();
  }

  private static Long randomDigit() {
    return Math.round(Math.random() * (MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER);
  }
}
