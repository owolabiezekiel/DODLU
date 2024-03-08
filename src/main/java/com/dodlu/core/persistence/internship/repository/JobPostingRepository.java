package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, String> {
  boolean existsByTitleIgnoreCaseAndEmployerId(String title, String employer);

  Page<JobPosting> findByEmployer(EmployerProfile employerProfile, Pageable pageable);
}
