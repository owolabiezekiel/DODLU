package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.app.utils.converters.StringListConverter;
import com.dodlu.core.domain.internship.enums.Country;
import com.dodlu.core.domain.internship.enums.InternshipLocation;
import com.dodlu.core.domain.internship.enums.State;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
@Table(name = "job_postings")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class JobPosting extends BaseEntity {
  @Column(nullable = false)
  private String title;

  private String description;
  private Integer durationInMonths;
  private Integer numberOfOpenPositions;
  private LocalDateTime expirationDate;
  private Boolean isTranscriptRequired;
  private Boolean isResumeRequired;
  private Boolean isCoverLetterRequired;
  private BigDecimal monthlyRemuneration;

  @Convert(converter = StringListConverter.class)
  private List<String> eligibleDepartments;

  @ManyToOne
  @JoinColumn(name = "employer_id", referencedColumnName = "id")
  private EmployerProfile employer;

  @Enumerated(EnumType.STRING)
  private InternshipLocation location;

  @Enumerated(EnumType.STRING)
  private State state;

  @Enumerated(EnumType.STRING)
  private Country country;

  @Column(nullable = false)
  private String address;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    JobPosting that = (JobPosting) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public JobPostingModel toModel() {
    return JobPostingModel.builder()
        .id(id)
        .title(title)
        .description(description)
        .durationInMonths(durationInMonths)
        .numberOfOpenPositions(numberOfOpenPositions)
        .expirationDate(expirationDate)
        .isTranscriptRequired(isTranscriptRequired)
        .isResumeRequired(isResumeRequired)
        .isCoverLetterRequired(isCoverLetterRequired)
        .monthlyRemuneration(monthlyRemuneration)
        .eligibleDepartments(eligibleDepartments)
        .location(location)
        .employer(employer.toModel())
        .build();
  }
}
