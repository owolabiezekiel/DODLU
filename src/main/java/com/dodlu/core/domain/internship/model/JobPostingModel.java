package com.dodlu.core.domain.internship.model;

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
  private EmployerProfileModel employer;
}
