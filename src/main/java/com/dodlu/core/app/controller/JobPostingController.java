package com.dodlu.core.app.controller;

import static com.dodlu.core.app.utils.MapperUtil.buildPageable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.PagingAndSortingParams;
import com.dodlu.core.app.model.filters.JobPostingFilter;
import com.dodlu.core.app.model.request.internship.CreateJobPostingRequest;
import com.dodlu.core.app.model.request.internship.JobPostingApplicationRequest;
import com.dodlu.core.app.model.response.JobPostingApplicationResult;
import com.dodlu.core.app.model.response.JobPostingResult;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.StudentProfileResult;
import com.dodlu.core.domain.internship.model.JobPostingApplicationModel;
import com.dodlu.core.domain.internship.model.JobPostingModel;
import com.dodlu.core.domain.internship.service.DefaultJobPostingService;
import com.dodlu.core.domain.user.model.StudentProfileModel;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/job-posting", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class JobPostingController {
  private final DefaultJobPostingService jobPostingService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Employer create job posting")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = JobPostingModel.class)})
  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
  public ResponseModel<JobPostingModel> createJobPosting(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateJobPostingRequest request) {
    JobPostingModel jobPostingModel = jobPostingService.createJobPosting(user, request).join();
    return new ResponseModel<>(jobPostingModel);
  }

  @GetMapping
  @Operation(description = "Get all job postings")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = JobPostingResult.class,
        responseContainer = "page")
  })
  public ResponseModel<JobPostingResult> getAllJobPostings(
      @ModelAttribute JobPostingFilter filter, @ModelAttribute PagingAndSortingParams paging) {
    Pageable pageable = buildPageable(paging);
    Page<JobPostingModel> jobPostingPage =
        jobPostingService.getAllJobPostings(pageable, filter).join();
    JobPostingResult jobPostingResult = new JobPostingResult(jobPostingPage);
    return new ResponseModel<>(jobPostingResult);
  }

  @GetMapping(value = "/me")
  @Operation(description = "Get all employer job postings")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = JobPostingResult.class,
        responseContainer = "page")
  })
  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
  public ResponseModel<JobPostingResult> getAllEmployerJobPostings(
      @AuthenticationPrincipal User user,
      @ModelAttribute PagingAndSortingParams paging,
      @ModelAttribute JobPostingFilter filter) {
    Pageable pageable = buildPageable(paging);
    Page<JobPostingModel> jobPostingPage =
        jobPostingService.getEmployerJobPostings(user, pageable, filter).join();
    JobPostingResult jobPostingResult = new JobPostingResult(jobPostingPage);
    return new ResponseModel<>(jobPostingResult);
  }

  @GetMapping(value = "/{id}")
  @Operation(description = "Get single job posting")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = JobPostingModel.class)})
  public ResponseModel<JobPostingModel> getJobPosting(@PathVariable("id") String id) {
    JobPostingModel jobPosting = jobPostingService.getJobPostingById(id).join();
    return new ResponseModel<>(jobPosting);
  }

  @PostMapping(value = "/{jobPostingId}/apply", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Student apply to job posting")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = JobPostingApplicationModel.class)
  })
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<JobPostingApplicationModel> applyToJobPosting(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody JobPostingApplicationRequest request,
      @PathVariable("jobPostingId") String jobPostingId) {
    JobPostingApplicationModel jobPostingApplication =
        jobPostingService.applyToJobPosting(user, request, jobPostingId).join();
    return new ResponseModel<>(jobPostingApplication);
  }

  @GetMapping(value = "/{jobPostingId}/applicants")
  @Operation(description = "Employer get all applicants to their job posting")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = StudentProfileResult.class,
        responseContainer = "page")
  })
  @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN')")
  public ResponseModel<StudentProfileResult> getJobPostingApplicants(
      @AuthenticationPrincipal User user,
      @ModelAttribute PagingAndSortingParams paging,
      @PathVariable("jobPostingId") String jobPostingId) {
    Pageable pageable = buildPageable(paging);
    Page<StudentProfileModel> applicantsPage =
        jobPostingService.getJobPostingApplicants(user, pageable, jobPostingId).join();
    StudentProfileResult applicantsResult = new StudentProfileResult(applicantsPage);
    return new ResponseModel<>(applicantsResult);
  }

  @PostMapping(value = "/{applicationId}/approve")
  @Operation(description = "Employer approve job posting application")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = JobPostingApplicationModel.class)
  })
  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
  public ResponseModel<JobPostingApplicationModel> approveJobPostingApplication(
      @AuthenticationPrincipal User user, @PathVariable("applicationId") String applicationId) {
    JobPostingApplicationModel jobPostingApplication =
        jobPostingService.approveJobPostingApplication(user, applicationId).join();
    return new ResponseModel<>(jobPostingApplication);
  }

  @GetMapping(value = "/applications")
  @Operation(description = "Get logged in student's job posting")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = JobPostingApplicationResult.class,
        responseContainer = "page")
  })
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<JobPostingApplicationResult> getJobPostingApplications(
      @AuthenticationPrincipal User user, @ModelAttribute PagingAndSortingParams paging) {
    Pageable pageable = buildPageable(paging);
    Page<JobPostingApplicationModel> jobPostingApplicationPage =
        jobPostingService.getUserJobApplications(user, pageable).join();
    JobPostingApplicationResult jobPostingApplicationResult =
        new JobPostingApplicationResult(jobPostingApplicationPage);
    return new ResponseModel<>(jobPostingApplicationResult);
  }
}
