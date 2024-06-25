package com.dodlu.core.app.model.request.immunization;

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
public class CreateParentRequest {
  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "firstName is required")
  private String firstName;

  @NotBlank(message = "lastName is required")
  private String lastName;

  @Email(message = "invalid parent email format")
  @NotNull(message = "email is required")
  private String email;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  private String phoneNumber;

  @NotNull(message = "date of birth is required")
  @PastOrPresent(message = "date of birth can not be in the future")
  private LocalDate dateOfBirth;
}
