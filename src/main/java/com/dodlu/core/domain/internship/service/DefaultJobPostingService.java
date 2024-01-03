package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.internship.enums.ApplicationStatus.ACCEPTED;
import static com.dodlu.core.domain.internship.enums.ApplicationStatus.PENDING;
import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.JobPostingUtils.checkForDuplicateJobTitle;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.internship.CreateJobPostingRequest;
import com.dodlu.core.app.model.request.internship.JobPostingApplicationRequest;
import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.utils.JobPostingUtils;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.internship.entity.JobPostingApplication;
import com.dodlu.core.persistence.internship.entity.Placement;
import com.dodlu.core.persistence.internship.repository.JobPostingApplicationRepository;
import com.dodlu.core.persistence.internship.repository.JobPostingRepository;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  private final PlacementRepository placementRepository;
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
          JobPosting jobPosting = getJobPosting(jobPostingId);

          StudentProfile applicant =
              getStudentProfileByUsername(
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

  @Transactional
  public CompletableFuture<Page<JobPostingApplicationModel>> getUserJobApplications(
      User user, Pageable pageable) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          return jobPostApplicationRepository
              .findJobPostingApplicationsByApplicantId(pageable, studentProfile.getId())
              .map(JobPostingApplication::toModel);
        });
  }

  @Transactional
  public CompletableFuture<Page<StudentProfileModel>> getJobPostingApplicants(
      User user, Pageable pageable, String jobId) {
    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());

          JobPosting jobPosting = getJobPosting(jobId);
          if (!jobPosting.getEmployer().getId().equals(employerProfile.getId())) {
            throw exceptionService.handleCreateUnAuthorized(
                "Only the job poster can view applicants");
          }

          Page<JobPostingApplication> pagedContent =
              jobPostApplicationRepository.findJobPostingApplicationByJobPostingId(
                  pageable, jobPosting.getId());

          List<StudentProfileModel> studentProfiles =
              pagedContent.stream()
                  .map(jobPostingApplication -> jobPostingApplication.getApplicant().toModel())
                  .toList();

          return new PageImpl<>(
              studentProfiles, pagedContent.getPageable(), pagedContent.getTotalElements());
        });
  }

  @Transactional
  public CompletableFuture<JobPostingApplicationModel> approveJobPostingApplication(
      User user, String jobApplicationId) {
    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());
          JobPostingApplication jobPostingApplication = getJobPostingApplication(jobApplicationId);

          doApproveJobValidations(jobPostingApplication, employerProfile);

          JobPosting jobPosting = getJobPosting(jobPostingApplication.getJobPosting().getId());
          int numberOfOpenPositions = jobPosting.getNumberOfOpenPositions();
          jobPosting.setNumberOfOpenPositions(numberOfOpenPositions - 1);
          jobPostingApplication.setApplicationStatus(ACCEPTED);

          Placement placement = buildPlacement(jobPostingApplication, jobPosting);

          jobPostingRepository.save(jobPosting);
          placementRepository.save(placement);
          return jobPostApplicationRepository.save(jobPostingApplication).toModel();
        });
  }

  private JobPosting getJobPosting(String id) {
    return JobPostingUtils.getJobPostingById(
        jobPostingRepository,
        () -> exceptionService.handleCreateNotFoundException("Job post does not exist"),
        id);
  }

  private JobPostingApplication getJobPostingApplication(String id) {
    return jobPostApplicationRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Application does not exist"));
  }

  private void doJobApplicationValidations(
      JobPostingApplicationRequest request, JobPosting jobPosting, StudentProfile applicant) {
    if (jobPostApplicationRepository.existsByJobPostingIdAndApplicantId(
        jobPosting.getId(), applicant.getId())) {
      throw exceptionService.handleCreateBadRequest("You have already applied to this job");
    }

    if (jobPosting.getNumberOfOpenPositions() < 1) {
      throw exceptionService.handleCreateBadRequest("This job does not have any open position");
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

  private void doApproveJobValidations(
      JobPostingApplication jobPostingApplication, EmployerProfile employerProfile) {
    if (!jobPostingApplication
        .getJobPosting()
        .getEmployer()
        .getId()
        .equals(employerProfile.getId())) {
      throw exceptionService.handleCreateUnAuthorized(
          "Only the job poster can approve this application");
    }
    if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED)) {
      throw (exceptionService.handleCreateBadRequest("Application has already been approved"));
    }
  }

  private void doPostingValidations(CreateJobPostingRequest request, EmployerProfile employer) {
    checkForDuplicateJobTitle(
        jobPostingRepository, exceptionService, request.getTitle(), employer.getId());
  }

  private Placement buildPlacement(
      JobPostingApplication jobPostingApplication, JobPosting jobPosting) {
    LocalDateTime validUntil = LocalDateTime.now().plusMonths(jobPosting.getDurationInMonths());
    return Placement.builder()
        .active(true)
        .validUntil(validUntil)
        .company(jobPosting.getEmployer().getCompany())
        .student(jobPostingApplication.getApplicant())
        .build();
  }
}
