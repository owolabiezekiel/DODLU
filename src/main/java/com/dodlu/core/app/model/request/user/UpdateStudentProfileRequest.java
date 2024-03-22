package com.dodlu.core.app.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
public class UpdateStudentProfileRequest {
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  protected String phoneNumber;

  @Email(message = "email not valid")
  protected String email;
}
