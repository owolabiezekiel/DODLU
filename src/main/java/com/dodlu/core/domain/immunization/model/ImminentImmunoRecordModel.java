package com.dodlu.core.domain.immunization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ImminentImmunoRecordModel {
  private String id;

  @JsonIgnoreProperties({"immunizations"})
  private ChildModel child;

  private ImmunizationModel immunization;
}
