package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Data
@FieldMatch(
    first = "newPassword",
    second = "confirmPassword",
    message = "Password and confirm password does not match")
public class ForgetPasswordRequest {
  @NotBlank(message = "Token is required")
  String token;

  @NotBlank(message = "New password is required")
  String newPassword;

  @NotBlank(message = "Confirmed new password is required")
  String confirmPassword;
}
