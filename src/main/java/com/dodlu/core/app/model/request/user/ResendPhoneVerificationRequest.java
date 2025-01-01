package com.dodlu.core.app.model.request.user;

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
public class ResendPhoneVerificationRequest {
  @NotBlank(message = "phoneNumber is required")
  @Size(min = 10, message = "phoneNumber must be at least 10 characters")
  private String phoneNumber;

  @NotBlank(message = "country cannot be blank")
  private String country;
}
