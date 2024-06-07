package com.dodlu.core.app.model.request.immunization;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
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
public class CreateImmunizationRequest {
  @NotNull(message = "vaccineId is required")
  private String vaccineId;

  @NotNull(message = "minimumAdministerDate is required")
  private LocalDate minimumAdministerDate;

  private boolean administered;

  @PastOrPresent(message = "dateOfAdministration can not be in the future")
  private LocalDate dateOfAdministration;
}
