package com.dodlu.core.app.model.request.user;

import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class UpdateCustomerProfileRequest {
  private String firstName;
  protected String lastName;
  private String middleName;

  @Past(message = "date of birth must be in the past")
  private LocalDate dateOfBirth;
}
