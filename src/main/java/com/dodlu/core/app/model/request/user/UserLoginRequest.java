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
public class UserLoginRequest {
  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "password is required")
  private String password;
}
