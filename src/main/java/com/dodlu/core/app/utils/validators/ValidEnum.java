package com.dodlu.core.app.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.METHOD,
  ElementType.FIELD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.PARAMETER,
  ElementType.TYPE_USE
})
@Constraint(validatedBy = {EnumValidator.class})
public @interface ValidEnum {
  String message() default "enum not valid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
