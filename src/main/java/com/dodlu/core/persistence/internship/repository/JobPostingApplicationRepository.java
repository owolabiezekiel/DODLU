package com.dodlu.core.persistence.internship.repository;

import static com.dodlu.core.domain.internship.enums.ApplicationStatus.PENDING;
import static com.dodlu.core.persistence.internship.entity.QJobPostingApplication.jobPostingApplication;

import com.dodlu.core.app.model.filters.JobPostingApplicationFilter;
import com.dodlu.core.domain.internship.enums.ApplicationStatus;
import com.dodlu.core.persistence.internship.entity.JobPostingApplication;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JobPostingApplicationRepository
    extends JpaRepository<JobPostingApplication, String>,
        QuerydslPredicateExecutor<JobPostingApplication> {
  boolean existsByJobPostingIdAndApplicantId(String jobPostingId, String applicantId);

  default Page<JobPostingApplication> findJobPostingApplicationsByApplicantId(
      Pageable pageable, String studentProfileId, JobPostingApplicationFilter filter) {
    BooleanExpression predicate = jobPostingApplication.applicant.id.eq(studentProfileId);

    if (filter.getStatus() != null) {
      predicate = predicate.and(jobPostingApplication.applicationStatus.eq(filter.getStatus()));
    }

    return findAll(predicate, pageable);
  }

  long countByJobPostingId(String jobPostingId);

  default Page<JobPostingApplication> findJobPostingApplicationByJobPostingId(
      Pageable pageable, JobPostingApplicationFilter filter, String jobPostingId) {
    ApplicationStatus status = filter.getStatus() == null ? PENDING : filter.getStatus();

    BooleanExpression predicate = jobPostingApplication.id.eq(jobPostingId);
    predicate = predicate.and(jobPostingApplication.applicationStatus.eq(status));

    return findAll(predicate, pageable);
  }
}
