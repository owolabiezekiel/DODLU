package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.JobPostingUtils.checkForDuplicateJobTitle;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.CreateJobPostingRequest;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.domain.utils.JobPostingUtils;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.internship.repository.JobPostingRepository;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJobPostingService {
  private final EmployerProfileRepository employerProfileRepository;
  private final JobPostingRepository jobPostingRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<JobPostingModel> createJobPosting(
      User user, CreateJobPostingRequest request) {
    return supplyAsync(
        () -> {
          EmployerProfile employer =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());
          doPostingValidations(request, employer);

          JobPosting jobPosting =
              JobPosting.builder()
                  .title(request.getTitle())
                  .description(request.getDescription())
                  .durationInMonths(request.getDurationInMonths())
                  .numberOfOpenPositions(request.getNumberOfOpenPositions())
                  .expirationDate(request.getExpirationDate())
                  .isCgpaRequired(request.getIsCgpaRequired())
                  .isResumeRequired(request.getIsResumeRequired())
                  .isCoverLetterRequired(request.getIsCoverLetterRequired())
                  .eligibleDepartments(new ArrayList<>(request.getEligibleDepartments()))
                  .employer(employer)
                  .build();

          return jobPostingRepository.save(jobPosting).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<JobPostingModel>> getAllJobPostings(Pageable pageable) {
    return supplyAsync(() -> jobPostingRepository.findAll(pageable).map(JobPosting::toModel));
  }

  @Transactional
  public CompletableFuture<JobPostingModel> getJobPostingById(String jobPostingId) {
    return supplyAsync(
        () ->
            JobPostingUtils.getJobPostingById(
                    jobPostingRepository,
                    () -> exceptionService.handleCreateNotFoundException("Job post does not exist"),
                    jobPostingId)
                .toModel());
  }

  private void doPostingValidations(CreateJobPostingRequest request, EmployerProfile employer) {
    checkForDuplicateJobTitle(
        jobPostingRepository, exceptionService, request.getTitle(), employer.getId());
  }
}
