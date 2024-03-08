package com.dodlu.core.app.model.filters;

import com.dodlu.core.domain.internship.enums.InternshipLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingFilters {
  private InternshipLocation location;
}
