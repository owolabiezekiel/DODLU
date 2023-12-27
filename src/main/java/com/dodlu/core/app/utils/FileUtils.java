package com.dodlu.core.app.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {
  public static Boolean isValidImageFormat(String format) {
    return switch (format.toLowerCase()) {
      case "jpg", "png", "jpeg" -> true;
      default -> false;
    };
  }

  public static Boolean isValidDocumentFormat(String format) {
    return switch (format.toLowerCase()) {
      case "pdf", "doc", "docx" -> true;
      default -> false;
    };
  }

  public static Boolean isValidWorksheetFormat(String format) {
    return switch (format.toLowerCase()) {
      case "pdf", "doc", "docx" -> true;
      default -> false;
    };
  }
}
