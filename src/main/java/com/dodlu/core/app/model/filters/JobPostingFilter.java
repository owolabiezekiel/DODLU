package com.dodlu.core.app.model.filters;

import com.dodlu.core.domain.internship.enums.Country;
import com.dodlu.core.domain.internship.enums.InternshipLocation;
import com.dodlu.core.domain.internship.enums.State;
import com.dodlu.core.domain.user.enums.Industry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingFilter {
  private InternshipLocation location;
  private State state;
  private Country country;
  private Industry industry;
  private Boolean active;
}
