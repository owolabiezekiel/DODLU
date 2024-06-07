package com.dodlu.core.domain.immunization.model;

import java.util.Collection;
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
public class ImmunizationRecordModel {
  private ChildModel child;
  private Collection<ImmunizationModel> immunizations;
}
