package com.dodlu.core.app.model.request.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CreateCountryRequest {
  @NotBlank(message = "name is required")
  @Pattern(
      regexp = "^[a-zA-Z]+$",
      message = " country name must contain only alphabetic characters")
  private String name;

  @NotBlank(message = "iso3Code is required")
  @Size(min = 3, max = 3, message = "length of iso3 code must be 3")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "iso3 code must contain only alphabetic characters")
  private String iso3Code;
}
