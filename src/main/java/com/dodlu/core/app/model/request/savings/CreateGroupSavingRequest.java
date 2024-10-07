package com.dodlu.core.app.model.request.savings;

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
public class CreateGroupSavingRequest {
  @NotBlank(message = "name cannot be blank")
  private String name;

  @NotBlank(message = "description cannot be blank")
  private String description;
}
