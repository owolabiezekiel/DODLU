package com.dodlu.core.domain.internship.service;

import static com.dodlu.core.app.utils.StringUtilities.isNullOrBlank;
import static com.dodlu.core.domain.internship.enums.ApplicationStatus.*;
import static com.dodlu.core.domain.utils.EmployerProfileUtils.getEmployerByUsername;
import static com.dodlu.core.domain.utils.JobPostingUtils.checkForDuplicateJobTitle;
import static com.dodlu.core.domain.utils.StudentProfileUtils.getStudentProfileByUsername;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.filters.JobPostingApplicationFilter;
import com.dodlu.core.app.model.filters.JobPostingFilter;
import com.dodlu.core.app.model.request.internship.CreateJobPostingRequest;
import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.domain.utils.JobPostingUtils;
import com.dodlu.core.persistence.internship.entity.JobPosting;
import com.dodlu.core.persistence.internship.entity.JobPostingApplication;
import com.dodlu.core.persistence.internship.entity.Placement;
import com.dodlu.core.persistence.internship.entity.StudentDocuments;
import com.dodlu.core.persistence.internship.repository.JobPostingApplicationRepository;
import com.dodlu.core.persistence.internship.repository.JobPostingRepository;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import com.dodlu.core.persistence.internship.repository.StudentDocumentRepository;
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
  private final StudentDocumentRepository studentDocumentRepository;
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
                  .isTranscriptRequired(request.getIsCgpaRequired())
                  .isResumeRequired(request.getIsResumeRequired())
                  .isCoverLetterRequired(request.getIsCoverLetterRequired())
                  .eligibleDepartments(new ArrayList<>(request.getEligibleDepartments()))
                  .monthlyRemuneration(request.getMonthlyRemuneration())
                  .industry(employer.getCompany().getIndustry())
                  .location(request.getLocation())
                  .state(request.getState())
                  .country(request.getCountry())
                  .address(request.getAddress())
                  .employer(employer)
                  .build();

          return jobPostingRepository.save(jobPosting).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<JobPostingModel>> getAllJobPostings(
      Pageable pageable, JobPostingFilter filter) {
    return supplyAsync(
        () -> jobPostingRepository.findAll(pageable, filter).map(JobPosting::toModel));
  }

  @Transactional
  public CompletableFuture<Page<JobPostingModel>> getAllJobPostingsForStudents(
      User user, Pageable pageable, JobPostingFilter filter) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          return jobPostingRepository
              .findAllForStudent(pageable, filter, studentProfile)
              .map(JobPosting::toModel);
        });
  }

  @Transactional
  public CompletableFuture<Page<JobPostingModel>> getEmployerJobPostings(
      User user, Pageable pageable, JobPostingFilter filter) {

    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());
          return jobPostingRepository
              .findByEmployer(employerProfile, pageable, filter)
              .map(JobPosting::toModel);
        });
  }

  @Transactional
  public CompletableFuture<JobPostingModel> getJobPostingById(String jobPostingId) {
    return supplyAsync(() -> getJobPosting(jobPostingId).toModel());
  }

  @Transactional
  public CompletableFuture<JobPostingApplicationModel> applyToJobPosting(
      User user, String jobPostingId) {
    return supplyAsync(
        () -> {
          JobPosting jobPosting = getJobPosting(jobPostingId);

          StudentProfile applicant =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());

          StudentDocuments studentDocuments =
              studentDocumentRepository
                  .findByStudent(applicant)
                  .orElse(StudentDocuments.builder().build());
          doJobApplicationValidations(studentDocuments, jobPosting, applicant);
          JobPostingApplication application =
              JobPostingApplication.builder()
                  .jobPosting(jobPosting)
                  .applicant(applicant)
                  .applicationStatus(PENDING)
                  .build();

          if (jobPosting.getIsResumeRequired()) {
            application.setResumeLink(studentDocuments.getResumeUrl());
          }
          if (jobPosting.getIsCoverLetterRequired()) {
            application.setCoverLetterLink(studentDocuments.getCoverLetterUrl());
          }
          if (jobPosting.getIsTranscriptRequired()) {
            application.setTranscriptLink(studentDocuments.getTranscriptUrl());
          }
          return jobPostApplicationRepository.save(application).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<JobPostingApplicationModel>> getUserJobApplications(
      User user, Pageable pageable, JobPostingApplicationFilter filter) {
    return supplyAsync(
        () -> {
          StudentProfile studentProfile =
              getStudentProfileByUsername(
                  studentProfileRepository, exceptionService, user.getUsername());
          return jobPostApplicationRepository
              .findJobPostingApplicationsByApplicantId(pageable, studentProfile.getId(), filter)
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
  public CompletableFuture<JobPostingApplicationModel> employerApproveJobPostingApplication(
      User user, String jobApplicationId) {
    return supplyAsync(
        () -> {
          EmployerProfile employerProfile =
              getEmployerByUsername(
                  employerProfileRepository, exceptionService, user.getUsername());
          JobPostingApplication jobPostingApplication = getJobPostingApplication(jobApplicationId);

          doApproveJobValidations(jobPostingApplication, employerProfile);

          JobPosting jobPosting = jobPostingApplication.getJobPosting();
          int numberOfOpenPositions = jobPosting.getNumberOfOpenPositions();
          jobPosting.setNumberOfOpenPositions(numberOfOpenPositions - 1);
          jobPostingApplication.setApplicationStatus(ACCEPTED_BY_EMPLOYER);
          jobPostingApplication.setStudentHasAccepted(false);

          jobPostingRepository.save(jobPosting);
          return jobPostApplicationRepository.save(jobPostingApplication).toModel();
        });
  }

  @Transactional
  public CompletableFuture<JobPostingApplicationModel> studentAcceptJobPostingOffer(
      User user, String jobApplicationId) {
    return supplyAsync(
        () -> {
          JobPostingApplication jobPostingApplication = getJobPostingApplication(jobApplicationId);
          JobPosting jobPosting = jobPostingApplication.getJobPosting();

          if (!user.getUsername()
              .equalsIgnoreCase(jobPostingApplication.getApplicant().getUsername())) {
            throw exceptionService.handleCreateBadRequest("Unauthorized");
          }
          if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED_BY_EMPLOYER)) {
            jobPostingApplication.setStudentHasAccepted(true);
            jobPostingApplication.setApplicationStatus(ACCEPTED_BY_STUDENT);
            Placement placement = buildPlacement(jobPostingApplication, jobPosting);
            placementRepository.save(placement);
            return jobPostApplicationRepository.save(jobPostingApplication).toModel();
          } else if (jobPostingApplication.getApplicationStatus().equals(REJECTED_BY_EMPLOYER)) {
            throw exceptionService.handleCreateBadRequest("Employer has rejected this application");
          } else if (jobPostingApplication.getApplicationStatus().equals(REJECTED_BY_STUDENT)) {
            throw exceptionService.handleCreateBadRequest(
                "You have previously rejected this offer. You cannot accept anymore");
          } else if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED_BY_STUDENT)) {
            throw exceptionService.handleCreateBadRequest(
                "You have previously accepted this offer");
          } else {
            throw exceptionService.handleCreateBadRequest(
                "Employer has not approved your application yet, please try later");
          }
        });
  }

  @Transactional
  public CompletableFuture<JobPostingApplicationModel> studentRejectJobPostingOffer(
      User user, String jobApplicationId) {
    return supplyAsync(
        () -> {
          JobPostingApplication jobPostingApplication = getJobPostingApplication(jobApplicationId);
          if (!user.getUsername()
              .equalsIgnoreCase(jobPostingApplication.getApplicant().getUsername())) {
            throw exceptionService.handleCreateBadRequest("Unauthorized");
          }
          if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED_BY_EMPLOYER)) {
            jobPostingApplication.setStudentHasAccepted(false);
            jobPostingApplication.setApplicationStatus(REJECTED_BY_STUDENT);
            return jobPostApplicationRepository.save(jobPostingApplication).toModel();
          } else if (jobPostingApplication.getApplicationStatus().equals(REJECTED_BY_EMPLOYER)) {
            throw exceptionService.handleCreateBadRequest("Employer has rejected this application");
          } else if (jobPostingApplication.getApplicationStatus().equals(REJECTED_BY_STUDENT)) {
            throw exceptionService.handleCreateBadRequest(
                "You have previously rejected this offer");
          } else if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED_BY_STUDENT)) {
            throw exceptionService.handleCreateBadRequest(
                "You have previously accepted this offer. You cannot reject anymore");
          } else {
            throw exceptionService.handleCreateBadRequest(
                "Employer has not approved your application yet, please try later");
          }
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
      StudentDocuments studentDocuments, JobPosting jobPosting, StudentProfile applicant) {
    if (jobPostApplicationRepository.existsByJobPostingIdAndApplicantId(
        jobPosting.getId(), applicant.getId())) {
      throw exceptionService.handleCreateBadRequest("You have already applied to this job");
    }

    if (jobPosting.getNumberOfOpenPositions() < 1) {
      throw exceptionService.handleCreateBadRequest("This job does not have any open position");
    }

    if (jobPosting.getIsResumeRequired() && !isNullOrBlank(studentDocuments.getResumeUrl())) {
      throw exceptionService.handleCreateBadRequest(
          "Resume is required for this job. Please upload resume in profile settings");
    }

    if (jobPosting.getIsCoverLetterRequired()
        && !isNullOrBlank(studentDocuments.getCoverLetterUrl())) {
      throw exceptionService.handleCreateBadRequest(
          "Cover letter is required for this job. Please upload resume in profile settings");
    }

    if (jobPosting.getIsTranscriptRequired()
        && !isNullOrBlank(studentDocuments.getTranscriptUrl())) {
      throw exceptionService.handleCreateBadRequest(
          "Transcript is required for this job. Please upload resume in profile settings");
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
    if (jobPostingApplication.getApplicationStatus().equals(ACCEPTED_BY_EMPLOYER)) {
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
