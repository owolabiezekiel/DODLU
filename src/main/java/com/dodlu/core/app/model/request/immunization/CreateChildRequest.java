package com.dodlu.core.app.model.request.immunization;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Gender;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class CreateChildRequest {
  @NotBlank(message = "firstName is required")
  private String firstName;

  @NotBlank(message = "lastName is required")
  private String lastName;

  @ValidEnum(message = "invalid gender type")
  @NotNull(message = "gender is required")
  private Gender gender;

  @NotNull(message = "date of birth is required")
  @PastOrPresent(message = "date of birth can not be in the future")
  private LocalDate dateOfBirth;
}
