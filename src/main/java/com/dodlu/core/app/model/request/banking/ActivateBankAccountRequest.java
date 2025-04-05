package com.dodlu.core.app.model.request.banking;

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
@Builder()
public class ActivateBankAccountRequest {
  @NotBlank(message = "firstname is required")
  private String bvnFirstName;

  @NotBlank(message = "lastname is required")
  private String bvnLastName;

  @NotBlank(message = "wallet pin is required")
  private String pin;
}
