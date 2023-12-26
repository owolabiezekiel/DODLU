package com.dodlu.core.app.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
  public EnumValidator() {}

  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    } else {
      return !value.toString().equalsIgnoreCase("UNKNOWN");
    }
  }
}
