package com.dodlu.core.domain.immunization.model;

import com.dodlu.core.domain.immunization.model.enums.AdministerRoute;
import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.domain.immunization.model.enums.DosageType;
import com.dodlu.core.domain.immunization.model.enums.VaccineType;
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
public class VaccineModel {
  private String id;
  private AgeTarget ageTarget;
  private VaccineType type;
  private Double dosage;
  private DosageType dosageType;
  private AdministerRoute routeOfAdministration;
  private String site;
}
