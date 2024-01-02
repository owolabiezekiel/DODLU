package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.JobPostingApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingApplicationRepository
    extends JpaRepository<JobPostingApplication, String> {
  boolean existsByJobPostingIdAndApplicantId(String jobPostingId, String applicantId);

  Page<JobPostingApplication> findJobPostingApplicationsByApplicantId(
      Pageable pageable, String studentProfileId);

  Page<JobPostingApplication> findJobPostingApplicationByJobPostingId(
      Pageable pageable, String jobPostingId);
}
