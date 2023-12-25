package com.dodlu.core.app.model.request.department;

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
public class CreateDepartmentRequest {
  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "facultyId is required")
  private String facultyId;
}
