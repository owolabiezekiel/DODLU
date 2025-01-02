package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.TokenType;
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
public class VerifyOtpRequest {
  @NotBlank(message = "phoneNumber is required")
  @Size(min = 10, message = "phoneNumber must be at least 10 characters")
  private String phoneNumber;

  @NotBlank(message = "country cannot be blank")
  private String country;

  @NotBlank(message = "passcode cannot be blank")
  private String passcode;

  @ValidEnum(message = "Invalid token type")
  private TokenType tokenType;
}
