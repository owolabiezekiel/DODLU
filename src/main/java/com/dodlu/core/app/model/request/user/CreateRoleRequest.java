package com.dodlu.core.app.model.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CreateRoleRequest {
  @NotBlank(message = "name cannot be blank")
  @NotNull(message = "name cannot be null")
  private String name;

  @NotBlank(message = "description cannot be blank")
  @NotNull(message = "description cannot be null")
  private String description;
}
