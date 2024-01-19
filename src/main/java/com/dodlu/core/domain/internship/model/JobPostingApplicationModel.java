package com.dodlu.core.domain.internship.model;

import com.dodlu.core.domain.internship.enums.ApplicationStatus;
import com.dodlu.core.domain.user.model.StudentProfileModel;
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
public class JobPostingApplicationModel {
  private String id;
  private JobPostingModel jobPosting;
  private StudentProfileModel applicant;
  private String resumeLink;
  private String coverLetterLink;
  private String transcriptLink;
  private Double cgpa;
  private ApplicationStatus applicationStatus;
  private String updates;
  private String rejectReason;
}
