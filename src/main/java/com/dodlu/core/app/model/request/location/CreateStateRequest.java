package com.dodlu.core.app.model.request.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CreateStateRequest {
  @NotBlank(message = "name is required")
  @Pattern(regexp = "^[a-zA-Z]+$", message = "state name must contain only alphabetic characters")
  private String name;

  @NotBlank(message = "country ID is required")
  private String countryId;
}
