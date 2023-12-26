package com.dodlu.core.app.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDateTimeValidator.class)
public @interface FutureDateTime {
  String message() default "Date must be in the future";

  int value() default 1; // Default to one day

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
