package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CreateUserRequest {
  @NotBlank(message = "username is required")
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
}
