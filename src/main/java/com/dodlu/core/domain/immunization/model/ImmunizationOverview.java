package com.dodlu.core.domain.immunization.model;

import com.dodlu.core.persistence.immunization.entity.ImmunizationRecord;
import java.time.LocalDate;
import java.util.Map;
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
public class ImmunizationOverview {
  private Map<LocalDate, ImmunizationRecord> pendingImmunizations;
}
