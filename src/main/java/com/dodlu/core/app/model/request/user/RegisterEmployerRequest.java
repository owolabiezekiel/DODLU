package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.FieldMatch;
import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Industry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@FieldMatch(
    first = "password",
    second = "confirmPassword",
    message = "Password and confirm password does not match")
public class RegisterEmployerRequest {
  @NotBlank(message = "username is required")
  @Size(min = 3, max = 150, message = "username must be between 3 - 250 characters")
  private String username;

  @NotBlank(message = "email is required")
  @Email(message = "email not valid")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  @NotBlank(message = "Confirm Password is required")
  @Size(min = 8, message = "Confirm Password must be at least 8 characters")
  private String confirmPassword;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  private String phoneNumber;

  @NotBlank(message = "firstName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String firstName;

  @NotBlank(message = "lastName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String lastName;

  @NotBlank(message = "companyName is required")
  @Size(min = 3, message = "phoneNumber must be at least 11 characters")
  private String companyName;

  @Size(min = 10, message = "vision must be at least 11 characters")
  private String vision;

  @Size(min = 10, message = "mission must be at least 10 characters")
  private String mission;

  @NotNull(message = "industry is required")
  @ValidEnum(message = "invalid industry")
  private Industry industry;
}
