package com.dodlu.core.persistence.internship.repository;

import static com.dodlu.core.persistence.internship.entity.QJobPosting.jobPosting;

import com.dodlu.core.app.model.filters.JobPostingFilter;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JobPostingRepository
    extends JpaRepository<JobPosting, String>, QuerydslPredicateExecutor<JobPosting> {
  boolean existsByTitleIgnoreCaseAndEmployerId(String title, String employer);

  default Page<JobPosting> findByEmployer(
      EmployerProfile employerProfile, Pageable pageable, JobPostingFilter filter) {
    BooleanExpression predicate = jobPosting.employer.eq(employerProfile);

    if (filter.getIndustry() != null) {
      predicate = jobPosting.industry.eq(filter.getIndustry());
    }
    if (filter.getLocation() != null) {
      predicate = jobPosting.location.eq(filter.getLocation());
    }
    if (filter.getState() != null) {
      predicate = jobPosting.state.eq(filter.getState());
    }
    if (filter.getCountry() != null) {
      predicate = jobPosting.country.eq(filter.getCountry());
    }

    return findAll(predicate, pageable);
  }

  default Page<JobPosting> findAll(Pageable pageable, JobPostingFilter filter) {
    BooleanExpression predicate = null;

    if (filter.getIndustry() != null) {
      predicate = jobPosting.industry.eq(filter.getIndustry());
    }
    if (filter.getLocation() != null) {
      predicate =
          predicate != null
              ? predicate.and(jobPosting.location.eq(filter.getLocation()))
              : jobPosting.location.eq(filter.getLocation());
    }
    if (filter.getState() != null) {
      predicate =
          predicate != null
              ? predicate.and(jobPosting.state.eq(filter.getState()))
              : jobPosting.state.eq(filter.getState());
    }
    if (filter.getCountry() != null) {
      predicate =
          predicate != null
              ? predicate.and(jobPosting.country.eq(filter.getCountry()))
              : jobPosting.country.eq(filter.getCountry());
    }

    if (predicate == null) {
      return findAll(pageable);
    } else {
      return findAll(predicate, pageable);
    }
  }
}
