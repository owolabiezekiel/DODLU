package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.internship.repository.JobPostingRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class JobPostingUtils {
  public static void checkForDuplicateJobTitle(
      JobPostingRepository jobPostingRepository,
      CoreExceptionService exceptionService,
      String title,
      String employerId) {
    if (jobPostingRepository.existsByTitleIgnoreCaseAndEmployerId(title, employerId)) {
      throw exceptionService.handleCreateBadRequest("Duplicate job title");
    }
  }

  public static JobPosting getJobPostingById(
      JobPostingRepository jobPostingRepository,
      Supplier<RuntimeException> errorSupplier,
      String id) {
    return jobPostingRepository.findById(id).orElseThrow(errorSupplier);
  }
}
