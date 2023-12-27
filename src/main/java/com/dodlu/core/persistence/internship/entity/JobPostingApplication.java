package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.enums.ApplicationStatus;
import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "job_posting_applications")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class JobPostingApplication extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "job_posting_id", referencedColumnName = "id")
  private JobPosting jobPosting;

  @ManyToOne
  @JoinColumn(name = "applicant_id", referencedColumnName = "id")
  private StudentProfile applicant;

  private String resumeLink;
  private String coverLetterLink;
  private Double cgpa;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus applicationStatus;

  private String updates;
  private String rejectReason;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JobPostingApplication that = (JobPostingApplication) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public JobPostingApplicationModel toModel() {
    return JobPostingApplicationModel.builder()
        .id(id)
        .jobPosting(jobPosting.toModel())
        .applicant(applicant.toModel())
        .resumeLink(resumeLink)
        .coverLetterLink(coverLetterLink)
        .cgpa(cgpa)
        .applicationStatus(applicationStatus)
        .updates(updates)
        .rejectReason(rejectReason)
        .build();
  }
}
