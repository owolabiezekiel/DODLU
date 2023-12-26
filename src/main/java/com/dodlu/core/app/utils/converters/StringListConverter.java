package com.dodlu.core.app.utils.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collection;

@Converter
public class StringListConverter implements AttributeConverter<Collection<String>, String> {
  private static final String DELIMITER = ",";

  @Override
  public String convertToDatabaseColumn(Collection<String> strings) {
    if (strings == null || strings.isEmpty()) {
      return null;
    }
    return String.join(DELIMITER, strings);
  }

  @Override
  public Collection<String> convertToEntityAttribute(String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }
    return Arrays.asList(str.split(DELIMITER));
  }
}
