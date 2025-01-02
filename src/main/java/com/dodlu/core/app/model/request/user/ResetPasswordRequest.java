package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.FieldMatch;
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
  @NotBlank(message = "phoneNumber is required")
  @Size(min = 10, message = "phoneNumber must be at least 10 characters")
  private String phoneNumber;

  @NotBlank(message = "country cannot be blank")
  private String country;

  @NotBlank(message = "passcode is required")
  private String passcode;

  @NotBlank(message = "new password is required")
  @Size(min = 8, message = "new password must be at least 8 characters")
  private String newPassword;

  @NotBlank(message = "confirm new password is required")
  @Size(min = 8, message = "confirm new password must be at least 8 characters")
  private String confirmNewPassword;
}
