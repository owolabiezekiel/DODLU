package com.dodlu.core.domain.internship.model;

import com.dodlu.core.domain.internship.enums.Country;
import com.dodlu.core.domain.internship.enums.InternshipLocation;
import com.dodlu.core.domain.internship.enums.State;
import com.dodlu.core.domain.user.enums.Industry;
import com.dodlu.core.domain.user.model.EmployerProfileModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
public class JobPostingModel {
  private String id;
  private String title;
  private String description;
  private Integer durationInMonths;
  private Integer numberOfOpenPositions;
  private LocalDateTime expirationDate;
  private Boolean isTranscriptRequired;
  private Boolean isResumeRequired;
  private Boolean isCoverLetterRequired;
  private List<String> eligibleDepartments;
  private BigDecimal monthlyRemuneration;
  private Industry industry;
  private InternshipLocation location;
  private State state;
  private Country country;
  private String address;
  private EmployerProfileModel employer;
}
