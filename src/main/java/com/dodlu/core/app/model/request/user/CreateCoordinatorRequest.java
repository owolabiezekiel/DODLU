package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class CreateCoordinatorRequest extends CreateProfileRequest {
  @NotBlank(message = "title is required")
  @Size(min = 2, max = 30, message = "title must be between 2 - 30 characters")
  private String title;

  @NotNull(message = "gender is required")
  @ValidEnum(message = "invalid gender")
  private Gender gender;

  @NotBlank(message = "departmentId is required")
  private String departmentId;
}
