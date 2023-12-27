package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.internship.enums.ApplicationStatus.PENDING;
import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.JobPostingUtils.checkForDuplicateJobTitle;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.CreateJobPostingRequest;
import com.dodlu.core.app.model.request.internship.JobPostingApplicationRequest;
import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.domain.utils.JobPostingUtils;
import com.dodlu.core.domain.utils.StudentProfileUtils;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.internship.entity.JobPostingApplication;
import com.dodlu.core.persistence.internship.repository.JobPostingApplicationRepository;
import com.dodlu.core.persistence.internship.repository.JobPostingRepository;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
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
  private final StudentProfileRepository studentProfileRepository;
  private final JobPostingRepository jobPostingRepository;
  private final JobPostingApplicationRepository jobPostApplicationRepository;
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
    return supplyAsync(() -> getJobPosting(jobPostingId).toModel());
  }

  @Transactional
  public CompletableFuture<JobPostingApplicationModel> applyToJobPosting(
      User user, JobPostingApplicationRequest request, String jobPostingId) {
    return supplyAsync(
        () -> {
          log.info("CGPA is null? {}, value is {}", request.getCgpa() == null, request.getCgpa());
          JobPosting jobPosting = getJobPosting(jobPostingId);
          log.info("is CGPA required for job? {}", jobPosting.getIsCgpaRequired());
          StudentProfile applicant =
              StudentProfileUtils.getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          doJobApplicationValidations(request, jobPosting, applicant);

          JobPostingApplication application =
              JobPostingApplication.builder()
                  .jobPosting(jobPosting)
                  .applicant(applicant)
                  .resumeLink(request.getResumeLink())
                  .coverLetterLink(request.getCoverLetterLink())
                  .cgpa(request.getCgpa())
                  .applicationStatus(PENDING)
                  .build();
          return jobPostApplicationRepository.save(application).toModel();
        });
  }

  private JobPosting getJobPosting(String id) {
    return JobPostingUtils.getJobPostingById(
        jobPostingRepository,
        () -> exceptionService.handleCreateNotFoundException("Job post does not exist"),
        id);
  }

  private void doJobApplicationValidations(
      JobPostingApplicationRequest request, JobPosting jobPosting, StudentProfile applicant) {
    if (jobPostApplicationRepository.existsByJobPostingIdAndApplicantId(
        jobPosting.getId(), applicant.getId())) {
      throw exceptionService.handleCreateBadRequest("You have already applied to this job");
    }

    if (jobPosting.getIsCgpaRequired() && request.getCgpa() == null) {
      throw exceptionService.handleCreateBadRequest("CGPA is required to apply to this job");
    }

    if (jobPosting.getIsResumeRequired() && isNullOrBlank(request.getResumeLink())) {
      throw exceptionService.handleCreateBadRequest("Resume/CV is required to apply to this job");
    }

    if (jobPosting.getIsCoverLetterRequired() && isNullOrBlank(request.getCoverLetterLink())) {
      throw exceptionService.handleCreateBadRequest(
          "Cover Letter is required to apply to this job");
    }
  }

  private void doPostingValidations(CreateJobPostingRequest request, EmployerProfile employer) {
    checkForDuplicateJobTitle(
        jobPostingRepository, exceptionService, request.getTitle(), employer.getId());
  }
}
