package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Industry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
public class UpdateEmployerRequest {
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  private String phoneNumber;

  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String firstName;

  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String lastName;

  @Size(min = 3, message = "phoneNumber must be at least 11 characters")
  private String companyName;

  @Size(max = 1000, message = "vision must not be more than 1000 characters")
  private String vision;

  @Size(max = 10000, message = "mission must not be more than 10000 characters")
  private String mission;

  @ValidEnum(message = "invalid industry")
  private Industry industry;

  @Email(message = "email not valid")
  protected String email;
}
