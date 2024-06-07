package com.dodlu.core.app.model.request.vaccines;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.immunization.model.enums.AdministerRoute;
import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.domain.immunization.model.enums.DosageType;
import com.dodlu.core.domain.immunization.model.enums.VaccineType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreateVaccineRequest {
  @NotNull(message = "ageTarget is required")
  @ValidEnum(message = "invalid age target")
  private AgeTarget ageTarget;

  @NotNull(message = "vaccine type is required")
  @ValidEnum(message = "invalid vaccine type")
  private VaccineType type;

  @NotNull(message = "dosage is required")
  @Positive(message = "dosage must be positive value")
  private Double dosage;

  @NotNull(message = "dosageType is required")
  @ValidEnum(message = "invalid dosageType")
  private DosageType dosageType;

  @NotNull(message = "administerRoute is required")
  @ValidEnum(message = "invalid administerRoute")
  private AdministerRoute administerRoute;

  @NotBlank(message = "administerSite is required")
  private String administerSite;
}
