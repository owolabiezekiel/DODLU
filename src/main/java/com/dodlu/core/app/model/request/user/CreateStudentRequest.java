package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.enums.Programme;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class CreateStudentRequest extends CreateProfileRequest {
  @NotBlank(message = "matriculation number is required")
  @Size(min = 3, max = 100, message = "matriculation number must be between 3 - 100 characters")
  @Pattern(
      regexp = "^[a-zA-Z0-9][a-zA-Z0-9_/\\-]*$",
      message =
          "matriculation number must start with a letter or digit and can only contain letters, digits, hyphens, forward/backward slashes and underscores.")
  protected String matricNumber;

  @NotNull(message = "gender is required")
  @ValidEnum(message = "invalid gender")
  private Gender gender;

  @NotNull(message = "programme is required")
  @ValidEnum(message = "invalid programme")
  private Programme programme;

  @NotNull(message = "level is required")
  @ValidEnum(message = "level programme")
  private Level level;

  @PositiveOrZero(message = "cgpa cannot be negative")
  private Double cgpa;
}
