package com.dodlu.core.app.model.request.faculty;

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
public class CreateFacultyRequest {
  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "deanId is required")
  private String deanId;
}
