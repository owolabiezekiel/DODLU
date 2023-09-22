package com.dodlu.core.app.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
  String message() default "{com.gameporte.commons.annotation.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String first();

  String second();
}
