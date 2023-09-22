package com.dodlu.core.app.utils.validators;

import static java.lang.String.format;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
  private String firstFieldName;
  private String secondFieldName;

  public void initialize(final FieldMatch constraintAnnotation) {
    firstFieldName = constraintAnnotation.first();
    secondFieldName = constraintAnnotation.second();
  }

  public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    try {
      final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
      final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
      return firstObj == null && secondObj == null
          || firstObj != null && firstObj.equals(secondObj);
    } catch (final Exception ex) {
      log.error(format("Error matching fields %s & %s", firstFieldName, secondFieldName), ex);
    }
    return true;
  }
}
