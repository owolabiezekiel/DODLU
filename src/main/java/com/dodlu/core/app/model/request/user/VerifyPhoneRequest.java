package com.dodlu.core.app.model.request.user;

import jakarta.validation.constraints.NotBlank;
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
public class VerifyPhoneRequest {
  @NotBlank(message = "phone number is required")
  private String phoneNumber;

  @NotBlank(message = "passcode is required")
  private String passcode;
}
