package com.dodlu.core.app.model.request.internship;

import jakarta.validation.constraints.*;
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
public class AssignSupervisorRequest {
  @NotBlank(message = "supervisorId is required")
  private String supervisorId;

  @NotBlank(message = "studentId is required")
  private String studentId;
}
