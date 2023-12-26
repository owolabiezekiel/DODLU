package com.dodlu.core.app.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureDateTimeValidator implements ConstraintValidator<FutureDateTime, LocalDateTime> {
  private int daysToAdd;

  @Override
  public void initialize(FutureDateTime constraintAnnotation) {
    this.daysToAdd = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
    if (dateTime == null) {
      return true;
    }
    LocalDateTime currentDatePlusDays = LocalDateTime.now().plusDays(daysToAdd);
    return dateTime.isEqual(currentDatePlusDays) || dateTime.isAfter(currentDatePlusDays);
  }
}
