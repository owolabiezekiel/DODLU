package com.dodlu.core.app.model.filters;

import com.dodlu.core.domain.internship.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingApplicationFilter {
  private ApplicationStatus status;
}
