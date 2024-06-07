package com.dodlu.core.domain.immunization.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImmunizationModel {
  private String id;
  private VaccineModel vaccine;
  private LocalDate minimumAdministerDate;
  private Boolean administered;
  private LocalDate dateOfAdministration;
}
