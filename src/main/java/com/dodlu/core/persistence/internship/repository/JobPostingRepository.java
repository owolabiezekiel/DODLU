package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, String> {
  boolean existsByTitleIgnoreCaseAndEmployerId(String title, String employer);
}
