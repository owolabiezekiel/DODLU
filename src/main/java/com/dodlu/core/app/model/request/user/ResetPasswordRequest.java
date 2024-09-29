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
    first = "newPassword",
    second = "confirmNewPassword",
    message = "New password and confirm new password do not match")
public class ResetPasswordRequest {
  @NotBlank(message = "email is required")
  @Email(message = "invalid email format")
  private String email;

  @NotBlank(message = "passcode is required")
  private String passcode;

  @NotBlank(message = "new password is required")
  @Size(min = 8, message = "new password must be at least 8 characters")
  private String newPassword;

  @NotBlank(message = "confirm new password is required")
  @Size(min = 8, message = "confirm new password must be at least 8 characters")
  private String confirmNewPassword;
}
