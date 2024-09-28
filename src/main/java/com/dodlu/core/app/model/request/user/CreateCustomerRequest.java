package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.FieldMatch;
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
public class CreateCustomerRequest {
  private String username;

  @NotBlank(message = "email is required")
  @Email(message = "email not valid")
  private String email;

  @NotBlank(message = "firstName is required")
  @Size(min = 8, max = 255, message = "firstname must be at least 2 characters")
  private String firstName;

  @NotBlank(message = "lastname is required")
  @Size(min = 8, max = 255, message = "last must be at least 2 characters")
  private String lastName;

  @Size(min = 8, max = 255, message = "middleName must be at least 2 characters")
  private String middleName;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 11, message = "phoneNumber must be at least 10 characters")
  private String phoneNumber;

  @NotNull(message = "country is required")
  private String country;

  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  @NotBlank(message = "Confirm Password is required")
  @Size(min = 8, message = "Confirm Password must be at least 8 characters")
  private String confirmPassword;
}
